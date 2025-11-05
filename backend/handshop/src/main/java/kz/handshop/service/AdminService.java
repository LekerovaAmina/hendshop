package kz.handshop.service;

import kz.handshop.dto.request.CreateCategoryRequest;
import kz.handshop.dto.response.CategoryResponse;
import kz.handshop.dto.response.MessageResponse;
import kz.handshop.dto.response.ProductResponse;
import kz.handshop.dto.response.SimpleUserResponse;
import kz.handshop.dto.response.ProductImageResponse;
import kz.handshop.entity.*;
import kz.handshop.exception.CategoryNotFoundException;
import kz.handshop.exception.ProductNotFoundException;
import kz.handshop.exception.ValidationException;
import kz.handshop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private GlobalCategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductModerationRepository moderationRepository;

    @Autowired
    private ProductReportRepository reportRepository;

    @Autowired
    private ProductImageRepository imageRepository;

    // 1111111111111111111111111111111111111 Категории

    @Transactional
    public CategoryResponse createGlobalCategory(CreateCategoryRequest request) {
        // Проверка на дубликаты
        if (categoryRepository.existsByName(request.getName())) {
            throw new ValidationException("Категория с таким названием уже существует");
        }

        GlobalCategory category = new GlobalCategory();
        category.setName(request.getName());
        category.setIconUrl(request.getIconUrl());
        category.setIsActive(true);

        category = categoryRepository.save(category);

        return convertToCategoryResponse(category);
    }

    public List<CategoryResponse> getAllCategories() {
        List<GlobalCategory> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::convertToCategoryResponse)
                .collect(Collectors.toList());
    }

    // 222222222222222222222222222222222222222 Модерация товаров

    public List<ProductResponse> getProductsForModeration() {
        List<Product> products = productRepository.findByStatus(ProductStatus.MODERATION);
        return products.stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponse approveProduct(Long productId, User admin) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар не найден"));

        if (product.getStatus() != ProductStatus.MODERATION) {
            throw new ValidationException("Можно одобрить только товары на модерации");
        }

        product.setStatus(ProductStatus.PUBLISHED);
        product = productRepository.save(product);

        // Создать запись в модерации
        ProductModeration moderation = new ProductModeration();
        moderation.setProduct(product);
        moderation.setAdmin(admin);
        moderation.setAction(ModerationAction.APPROVE);
        moderationRepository.save(moderation);

        return convertToProductResponse(product);
    }

    @Transactional
    public ProductResponse rejectProduct(Long productId, String comment, User admin) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар не найден"));

        if (product.getStatus() != ProductStatus.MODERATION) {
            throw new ValidationException("Можно отклонить только товары на модерации");
        }

        if (comment == null || comment.trim().isEmpty()) {
            throw new ValidationException("Комментарий обязателен при отклонении");
        }

        product.setStatus(ProductStatus.EDIT_MODERATION);
        product = productRepository.save(product);

        // Создать запись в модерации
        ProductModeration moderation = new ProductModeration();
        moderation.setProduct(product);
        moderation.setAdmin(admin);
        moderation.setAction(ModerationAction.REJECT);
        moderation.setComment(comment);
        moderationRepository.save(moderation);

        return convertToProductResponse(product);
    }

    // 3333333333333333333333333333333333333333333 Жалобы

    public List<ProductReport> getUnreviewedReports() {
        return reportRepository.findByIsReviewedFalse();
    }

    @Transactional
    public MessageResponse rejectReports(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар не найден"));

        if (product.getStatus() != ProductStatus.BLOCKED) {
            throw new ValidationException("Товар не заблокирован");
        }

        // Вернуть статус PUBLISHED
        product.setStatus(ProductStatus.PUBLISHED);
        product.setReportsCount(0);
        productRepository.save(product);

        // Пометить все жалобы как проверенные
        List<ProductReport> reports = reportRepository.findByProductId(productId);
        reports.forEach(report -> report.setIsReviewed(true));
        reportRepository.saveAll(reports);

        return new MessageResponse("Жалобы отклонены, товар разблокирован");
    }

    @Transactional
    public MessageResponse acceptReports(Long productId, User admin) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар не найден"));

        if (product.getStatus() != ProductStatus.BLOCKED) {
            throw new ValidationException("Товар не заблокирован");
        }

        // Удалить товар
        product.setStatus(ProductStatus.DELETED);
        productRepository.save(product);

        // Пометить все жалобы как проверенные
        List<ProductReport> reports = reportRepository.findByProductId(productId);
        reports.forEach(report -> report.setIsReviewed(true));
        reportRepository.saveAll(reports);

        // Создать запись в модерации
        ProductModeration moderation = new ProductModeration();
        moderation.setProduct(product);
        moderation.setAdmin(admin);
        moderation.setAction(ModerationAction.DELETE);
        moderationRepository.save(moderation);

        return new MessageResponse("Жалобы приняты, товар удалён");
    }

    // 4444444444444444444444444444444 Конвертация

    private CategoryResponse convertToCategoryResponse(GlobalCategory category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setIconUrl(category.getIconUrl());
        response.setIsActive(category.getIsActive());
        return response;
    }

    private ProductResponse convertToProductResponse(Product product) {
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

        // Freelancer info
        if (product.getFreelancer() != null) {
            SimpleUserResponse freelancer = new SimpleUserResponse();
            freelancer.setId(product.getFreelancer().getId());
            freelancer.setUsername(product.getFreelancer().getUsername());
            freelancer.setAvatarUrl(product.getFreelancer().getAvatarUrl());
            response.setFreelancer(freelancer);
        }

        // Images
        List<ProductImage> images = imageRepository.findByProduct(product);
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
}