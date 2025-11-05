package kz.handshop.service;

import kz.handshop.dto.request.CreateProductRequest;
import kz.handshop.dto.response.*;
import kz.handshop.entity.*;
import kz.handshop.exception.ForbiddenException;
import kz.handshop.exception.InvalidStatusException;
import kz.handshop.exception.ProductNotFoundException;
import kz.handshop.exception.ShelfNotFoundException;
import kz.handshop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FreelancerShelfRepository shelfRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductModerationRepository moderationRepository;

    @Autowired
    private ProductReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    // Получить все опубликованные товары (публичный endpoint)
    public List<ProductResponse> getAllPublishedProducts(Long categoryId, String search) {
        List<Product> products = productRepository.findPublishedProducts(categoryId, search);
        return products.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Получить товар по ID
    @Transactional
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Товар не найден"));

        // Увеличить счётчик просмотров
        product.setViewsCount(product.getViewsCount() + 1);
        productRepository.save(product);

        return convertToDetailedResponse(product);
    }

    // Создать товар (DRAFT)
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request, User freelancer) {
        Product product = new Product();
        product.setFreelancer(freelancer);
        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setMaterials(request.getMaterials());
        product.setPrice(request.getPrice());
        product.setProductionTime(request.getProductionTime());
        product.setDeliveryType(DeliveryType.valueOf(request.getDeliveryType()));
        product.setStatus(ProductStatus.DRAFT);

        if (request.getShelfId() != null) {
            FreelancerShelf shelf = shelfRepository.findById(request.getShelfId())
                    .orElseThrow(() -> new ShelfNotFoundException("Полка не найдена"));

            if (!shelf.getFreelancer().getId().equals(freelancer.getId())) {
                throw new ForbiddenException("Полка принадлежит другому фрилансеру");
            }

            product.setShelf(shelf);
        }

        product = productRepository.save(product);
        return convertToResponse(product);
    }

    // Обновить товар
    @Transactional
    public ProductResponse updateProduct(Long productId, CreateProductRequest request, User freelancer) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар не найден"));

        if (!product.getFreelancer().getId().equals(freelancer.getId())) {
            throw new ForbiddenException("Вы не можете редактировать чужой товар");
        }

        if (product.getStatus() != ProductStatus.DRAFT && product.getStatus() != ProductStatus.EDIT_MODERATION) {
            throw new InvalidStatusException("Можно редактировать только товары в статусе DRAFT или EDIT_MODERATION");
        }

        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setMaterials(request.getMaterials());
        product.setPrice(request.getPrice());
        product.setProductionTime(request.getProductionTime());
        product.setDeliveryType(DeliveryType.valueOf(request.getDeliveryType()));

        if (request.getShelfId() != null) {
            FreelancerShelf shelf = shelfRepository.findById(request.getShelfId())
                    .orElseThrow(() -> new ShelfNotFoundException("Полка не найдена"));
            product.setShelf(shelf);
        }

        product = productRepository.save(product);
        return convertToResponse(product);
    }

    // Отправить товар на модерацию
    @Transactional
    public ProductResponse submitForModeration(Long productId, User freelancer) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар не найден"));

        if (!product.getFreelancer().getId().equals(freelancer.getId())) {
            throw new ForbiddenException("Вы не можете модерировать чужой товар");
        }

        if (product.getStatus() != ProductStatus.DRAFT && product.getStatus() != ProductStatus.EDIT_MODERATION) {
            throw new InvalidStatusException("Можно отправить на модерацию только товары в статусе DRAFT или EDIT_MODERATION");
        }

        product.setStatus(ProductStatus.MODERATION);
        product = productRepository.save(product);

        // Создать запись в модерации
        ProductModeration moderation = new ProductModeration();
        moderation.setProduct(product);
        moderation.setAdmin(null);
        moderation.setAction(ModerationAction.SUBMIT);
        moderationRepository.save(moderation);

        return convertToResponse(product);
    }

    // Архивировать товар
    @Transactional
    public ProductResponse archiveProduct(Long productId, User freelancer) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар не найден"));

        if (!product.getFreelancer().getId().equals(freelancer.getId())) {
            throw new ForbiddenException("Вы не можете архивировать чужой товар");
        }

        if (product.getStatus() != ProductStatus.PUBLISHED) {
            throw new InvalidStatusException("Можно архивировать только опубликованные товары");
        }

        product.setStatus(ProductStatus.ARCHIVED);
        product = productRepository.save(product);

        return convertToResponse(product);
    }

    // Восстановить из архива
    @Transactional
    public ProductResponse restoreProduct(Long productId, User freelancer) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар не найден"));

        if (!product.getFreelancer().getId().equals(freelancer.getId())) {
            throw new ForbiddenException("Вы не можете восстановить чужой товар");
        }

        if (product.getStatus() != ProductStatus.ARCHIVED) {
            throw new InvalidStatusException("Можно восстановить только архивные товары");
        }

        product.setStatus(ProductStatus.PUBLISHED);
        product = productRepository.save(product);

        return convertToResponse(product);
    }

    // Получить товары фрилансера
    public List<ProductResponse> getFreelancerProducts(User freelancer, ProductStatus status) {
        List<Product> products;
        if (status != null) {
            products = productRepository.findByFreelancerIdAndStatus(freelancer.getId(), status);
        } else {
            products = productRepository.findByFreelancer(freelancer);
        }
        return products.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Конвертация в Response
    private ProductResponse convertToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setTitle(product.getTitle());
        response.setDescription(product.getDescription());
        response.setMaterials(product.getMaterials());
        response.setPrice(product.getPrice());
        response.setProductionTime(product.getProductionTime());
        response.setDeliveryType(product.getDeliveryType() != null ? product.getDeliveryType().name() : null);
        response.setStatus(product.getStatus().name());
        response.setViewsCount(product.getViewsCount());
        response.setReportsCount(product.getReportsCount());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());

        // Freelancer
        SimpleUserResponse freelancerResponse = new SimpleUserResponse();
        freelancerResponse.setId(product.getFreelancer().getId());
        freelancerResponse.setUsername(product.getFreelancer().getUsername());
        freelancerResponse.setAvatarUrl(product.getFreelancer().getAvatarUrl());
        response.setFreelancer(freelancerResponse);

        // Images
        List<ProductImage> images = productImageRepository.findByProduct(product);
        List<ProductImageResponse> imageResponses = images.stream()
                .map(img -> {
                    ProductImageResponse imgResp = new ProductImageResponse();
                    imgResp.setId(img.getId());
                    imgResp.setImageUrl(img.getImageUrl());
                    imgResp.setIsPrimary(img.getIsPrimary());
                    imgResp.setOrderIndex(img.getOrderIndex());
                    return imgResp;
                })
                .collect(Collectors.toList());
        response.setImages(imageResponses);

        return response;
    }

    private ProductResponse convertToDetailedResponse(Product product) {
        ProductResponse response = convertToResponse(product);

        // Добавить рейтинг и отзывы
        BigDecimal avgRating = reviewRepository.getAverageRatingByProductId(product.getId());
        response.setAverageRating(avgRating);

        List<ProductReview> reviews = reviewRepository.findByProduct(product);
        response.setReviewsCount(reviews.size());

        return response;
    }
}