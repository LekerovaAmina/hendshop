package kz.handshop.service;

import kz.handshop.exception.*;
import kz.handshop.model.*;
import kz.handshop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductModerationRepository moderationRepository;
    private final ProductReportRepository reportRepository;
    private final UserRepository userRepository;

    // ===============================
    // CRUD операции
    // ===============================

    /**
     * Создание нового товара (черновик)
     */
    @Transactional
    public Product createProduct(Product product, Long freelancerId) {
        User freelancer = userRepository.findById(freelancerId)
                .orElseThrow(() -> new UserNotFoundException("Фрилансер не найден"));

        if (!freelancer.isFreelancer()) {
            throw new ForbiddenException("Только фрилансеры могут создавать товары");
        }

        product.setFreelancer(freelancer);
        product.setStatus(Product.ProductStatus.DRAFT);
        product.setViewsCount(0);
        product.setReportsCount(0);

        return productRepository.save(product);
    }

    /**
     * Обновление товара (только в статусах DRAFT или EDIT_MODERATION)
     */
    @Transactional
    public Product updateProduct(Long productId, Product updatedProduct, Long freelancerId) {
        Product product = getProductById(productId);

        // Проверка прав доступа
        if (!product.getFreelancer().getId().equals(freelancerId)) {
            throw new ForbiddenException("Вы не можете редактировать этот товар");
        }

        // Можно редактировать только черновики и товары на доработке
        if (product.getStatus() != Product.ProductStatus.DRAFT
                && product.getStatus() != Product.ProductStatus.EDIT_MODERATION) {
            throw new InvalidStatusException("Товар нельзя редактировать в текущем статусе");
        }

        // Обновление полей
        product.setTitle(updatedProduct.getTitle());
        product.setDescription(updatedProduct.getDescription());
        product.setMaterials(updatedProduct.getMaterials());
        product.setPrice(updatedProduct.getPrice());
        product.setProductionTime(updatedProduct.getProductionTime());
        product.setDeliveryType(updatedProduct.getDeliveryType());

        return productRepository.save(product);
    }

    /**
     * Получение товара по ID
     */
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Товар не найден"));
    }

    /**
     * Получение всех опубликованных товаров
     */
    public List<Product> getAllPublishedProducts() {
        return productRepository.findByStatus(Product.ProductStatus.PUBLISHED);
    }

    /**
     * Получение товаров фрилансера
     */
    public List<Product> getFreelancerProducts(Long freelancerId) {
        return productRepository.findByFreelancerId(freelancerId);
    }

    // ===============================
    // Управление статусами
    // ===============================

    /**
     * Отправка товара на модерацию
     */
    @Transactional
    public Product submitForModeration(Long productId, Long freelancerId) {
        Product product = getProductById(productId);

        // Проверка прав
        if (!product.getFreelancer().getId().equals(freelancerId)) {
            throw new ForbiddenException("Вы не можете отправить этот товар на модерацию");
        }

        // Проверка статуса
        if (!product.canSubmitForModeration()) {
            throw new InvalidStatusException("Товар нельзя отправить на модерацию. Проверьте заполненность всех полей.");
        }

        // Смена статуса
        product.setStatus(Product.ProductStatus.MODERATION);
        product = productRepository.save(product);

        // Запись в историю модерации
        ProductModeration moderation = new ProductModeration();
        moderation.setProduct(product);
        moderation.setAction(ProductModeration.ModerationAction.SUBMIT);
        moderationRepository.save(moderation);

        return product;
    }

    /**
     * Одобрение товара админом
     */
    @Transactional
    public Product approveProduct(Long productId, Long adminId) {
        Product product = getProductById(productId);
        User admin = getUserById(adminId);

        if (!admin.isAdmin()) {
            throw new ForbiddenException("Только админы могут одобрять товары");
        }

        if (product.getStatus() != Product.ProductStatus.MODERATION) {
            throw new InvalidStatusException("Товар не находится на модерации");
        }

        product.setStatus(Product.ProductStatus.PUBLISHED);
        product = productRepository.save(product);

        // Запись в историю
        ProductModeration moderation = new ProductModeration();
        moderation.setProduct(product);
        moderation.setAdmin(admin);
        moderation.setAction(ProductModeration.ModerationAction.APPROVE);
        moderationRepository.save(moderation);

        return product;
    }

    /**
     * Отклонение товара админом
     */
    @Transactional
    public Product rejectProduct(Long productId, Long adminId, String comment) {
        Product product = getProductById(productId);
        User admin = getUserById(adminId);

        if (!admin.isAdmin()) {
            throw new ForbiddenException("Только админы могут отклонять товары");
        }

        if (product.getStatus() != Product.ProductStatus.MODERATION) {
            throw new InvalidStatusException("Товар не находится на модерации");
        }

        if (comment == null || comment.trim().isEmpty()) {
            throw new ValidationException("Комментарий обязателен при отклонении товара");
        }

        product.setStatus(Product.ProductStatus.EDIT_MODERATION);
        product = productRepository.save(product);

        // Запись в историю
        ProductModeration moderation = new ProductModeration();
        moderation.setProduct(product);
        moderation.setAdmin(admin);
        moderation.setAction(ProductModeration.ModerationAction.REJECT);
        moderation.setComment(comment);
        moderationRepository.save(moderation);

        return product;
    }

    /**
     * Архивирование товара
     */
    @Transactional
    public Product archiveProduct(Long productId, Long freelancerId) {
        Product product = getProductById(productId);

        if (!product.getFreelancer().getId().equals(freelancerId)) {
            throw new ForbiddenException("Вы не можете архивировать этот товар");
        }

        if (product.getStatus() != Product.ProductStatus.PUBLISHED) {
            throw new InvalidStatusException("Можно архивировать только опубликованные товары");
        }

        product.setStatus(Product.ProductStatus.ARCHIVED);
        return productRepository.save(product);
    }

    /**
     * Восстановление из архива
     */
    @Transactional
    public Product restoreFromArchive(Long productId, Long freelancerId) {
        Product product = getProductById(productId);

        if (!product.getFreelancer().getId().equals(freelancerId)) {
            throw new ForbiddenException("Вы не можете восстановить этот товар");
        }

        if (product.getStatus() != Product.ProductStatus.ARCHIVED) {
            throw new InvalidStatusException("Товар не находится в архиве");
        }

        product.setStatus(Product.ProductStatus.PUBLISHED);
        return productRepository.save(product);
    }

    /**
     * Разблокировка товара админом (жалобы необоснованы)
     */
    @Transactional
    public Product unblockProduct(Long productId, Long adminId) {
        Product product = getProductById(productId);
        User admin = getUserById(adminId);

        if (!admin.isAdmin()) {
            throw new ForbiddenException("Только админы могут разблокировать товары");
        }

        if (product.getStatus() != Product.ProductStatus.BLOCKED) {
            throw new InvalidStatusException("Товар не заблокирован");
        }

        product.setStatus(Product.ProductStatus.PUBLISHED);
        product.setReportsCount(0); // Сброс счётчика жалоб
        product = productRepository.save(product);

        // Помечаем все жалобы как проверенные
        List<ProductReport> reports = reportRepository.findByProductId(productId);
        reports.forEach(report -> report.setIsReviewed(true));
        reportRepository.saveAll(reports);

        // Запись в историю
        ProductModeration moderation = new ProductModeration();
        moderation.setProduct(product);
        moderation.setAdmin(admin);
        moderation.setAction(ProductModeration.ModerationAction.UNBLOCK);
        moderationRepository.save(moderation);

        return product;
    }

    /**
     * Удаление товара админом (после блокировки)
     */
    @Transactional
    public Product deleteProduct(Long productId, Long adminId) {
        Product product = getProductById(productId);
        User admin = getUserById(adminId);

        if (!admin.isAdmin()) {
            throw new ForbiddenException("Только админы могут удалять товары");
        }

        if (product.getStatus() != Product.ProductStatus.BLOCKED) {
            throw new InvalidStatusException("Можно удалить только заблокированные товары");
        }

        product.setStatus(Product.ProductStatus.DELETED);
        product = productRepository.save(product);

        // Запись в историю
        ProductModeration moderation = new ProductModeration();
        moderation.setProduct(product);
        moderation.setAdmin(admin);
        moderation.setAction(ProductModeration.ModerationAction.DELETE);
        moderationRepository.save(moderation);

        return product;
    }

    // ===============================
    // Helper методы
    // ===============================

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    /**
     * Увеличение счётчика просмотров
     */
    @Transactional
    public void incrementViews(Long productId) {
        Product product = getProductById(productId);
        product.incrementViews();
        productRepository.save(product);
    }
}