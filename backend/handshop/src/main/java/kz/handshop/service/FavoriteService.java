package kz.handshop.service;

import kz.handshop.dto.response.MessageResponse;
import kz.handshop.dto.response.ProductResponse;
import kz.handshop.dto.response.SimpleUserResponse;
import kz.handshop.dto.response.ProductImageResponse;
import kz.handshop.entity.*;
import kz.handshop.exception.ProductNotFoundException;
import kz.handshop.exception.ValidationException;
import kz.handshop.repository.FavoriteRepository;
import kz.handshop.repository.ProductRepository;
import kz.handshop.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Transactional
    public MessageResponse addToFavorites(Long productId, User user) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар не найден"));

        // Проверка, что товар опубликован
        if (product.getStatus() != ProductStatus.PUBLISHED) {
            throw new ValidationException("Можно добавить в избранное только опубликованные товары");
        }

        // Проверка, что товар не добавлен уже
        if (favoriteRepository.existsByUserAndProduct(user, product)) {
            throw new ValidationException("Товар уже в избранном");
        }

        Favorite favorite = new Favorite(user, product);
        favoriteRepository.save(favorite);

        return new MessageResponse("Товар добавлен в избранное");
    }

    @Transactional
    public MessageResponse removeFromFavorites(Long productId, User user) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар не найден"));

        if (!favoriteRepository.existsByUserAndProduct(user, product)) {
            throw new ValidationException("Товар не найден в избранном");
        }

        favoriteRepository.deleteByUserAndProduct(user, product);

        return new MessageResponse("Товар удалён из избранного");
    }

    public List<ProductResponse> getUserFavorites(User user) {
        List<Favorite> favorites = favoriteRepository.findByUser(user);

        return favorites.stream()
                .map(favorite -> convertToProductResponse(favorite.getProduct()))
                .collect(Collectors.toList());
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
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());

        // Freelancer info
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
}