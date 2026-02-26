package kz.handshop.service;

import kz.handshop.dto.request.CreateProductRequest;
import kz.handshop.entity.*;
import kz.handshop.exception.ForbiddenException;
import kz.handshop.exception.ProductNotFoundException;
import kz.handshop.exception.ShelfNotFoundException;
import kz.handshop.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Unit Tests")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private FreelancerShelfRepository shelfRepository;

    @Mock
    private ProductImageRepository productImageRepository;

    @Mock
    private ProductModerationRepository moderationRepository;

    @Mock
    private ProductReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProductService productService;

    private User freelancer;
    private Product product;
    private CreateProductRequest createRequest;

    @BeforeEach
    void setUp() {
        freelancer = new User("freelancer@example.com", "freelancer1", "hash");
        freelancer.setId(1L);
        freelancer.setRole(UserRole.FREELANCER);

        product = new Product(freelancer, "Test Product", BigDecimal.valueOf(99.99));
        product.setId(1L);
        product.setStatus(ProductStatus.DRAFT);
        product.setFreelancer(freelancer);
        product.setViewsCount(0);

        createRequest = new CreateProductRequest();
        createRequest.setTitle("Test Product");
        createRequest.setDescription("Description");
        createRequest.setPrice(BigDecimal.valueOf(99.99));
        createRequest.setProductionTime(5);
        createRequest.setDeliveryType("KAZPOST");
    }

    @Test
    @DisplayName("getAllPublishedProducts - returns list")
    void getAllPublishedProducts_returnsList() {
        Product published = new Product();
        published.setId(1L);
        published.setTitle("Published");
        published.setStatus(ProductStatus.PUBLISHED);
        published.setFreelancer(freelancer);
        published.setViewsCount(0);
        when(productRepository.findPublishedProducts(null, null)).thenReturn(List.of(published));
        when(productImageRepository.findByProduct(any(Product.class))).thenReturn(Collections.emptyList());

        var result = productService.getAllPublishedProducts(null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Published");
        verify(productRepository).findPublishedProducts(null, null);
    }

    @Test
    @DisplayName("getProductById - not found throws")
    void getProductById_notFound_throwsProductNotFoundException() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(999L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("Товар не найден");

        verify(productRepository).findById(999L);
    }

    @Test
    @DisplayName("getProductById - success increments views")
    void getProductById_success_incrementsViewsAndReturns() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productImageRepository.findByProduct(any(Product.class))).thenReturn(Collections.emptyList());
        when(reviewRepository.getAverageRatingByProductId(1L)).thenReturn(BigDecimal.ZERO);
        when(reviewRepository.findByProduct(any(Product.class))).thenReturn(Collections.emptyList());

        product.setViewsCount(5);
        var result = productService.getProductById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getViewsCount()).isEqualTo(6);
        verify(productRepository).save(argThat(p -> p.getViewsCount() == 6));
    }

    @Test
    @DisplayName("createProduct - success without shelf")
    void createProduct_successWithoutShelf_returnsProductResponse() {
        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setTitle(createRequest.getTitle());
        savedProduct.setStatus(ProductStatus.DRAFT);
        savedProduct.setFreelancer(freelancer);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
        when(productImageRepository.findByProduct(any(Product.class))).thenReturn(Collections.emptyList());

        var result = productService.createProduct(createRequest, freelancer);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Product");
        assertThat(result.getStatus()).isEqualTo("DRAFT");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("createProduct - shelf not found throws")
    void createProduct_shelfNotFound_throwsShelfNotFoundException() {
        createRequest.setShelfId(999L);
        when(shelfRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.createProduct(createRequest, freelancer))
                .isInstanceOf(ShelfNotFoundException.class)
                .hasMessageContaining("Полка не найдена");

        verify(shelfRepository).findById(999L);
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("createProduct - shelf belongs to another freelancer throws")
    void createProduct_shelfBelongsToAnotherFreelancer_throwsForbiddenException() {
        User otherFreelancer = new User();
        otherFreelancer.setId(2L);
        FreelancerShelf shelf = new FreelancerShelf();
        shelf.setId(1L);
        shelf.setFreelancer(otherFreelancer);
        createRequest.setShelfId(1L);
        when(shelfRepository.findById(1L)).thenReturn(Optional.of(shelf));

        assertThatThrownBy(() -> productService.createProduct(createRequest, freelancer))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("Полка принадлежит другому фрилансеру");

        verify(shelfRepository).findById(1L);
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateProduct - product not found throws")
    void updateProduct_productNotFound_throwsProductNotFoundException() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.updateProduct(999L, createRequest, freelancer))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("Товар не найден");

        verify(productRepository).findById(999L);
    }
}
