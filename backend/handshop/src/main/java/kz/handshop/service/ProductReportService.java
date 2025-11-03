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
public class ProductReportService {

    private final ProductReportRepository reportRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    /**
     * Создание жалобы на товар
     */
    @Transactional
    public ProductReport createReport(Long productId, Long userId,
                                      ProductReport.ReportReason reason, String comment) {

        // Проверка существования товара
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар не найден"));

        // Проверка существования пользователя
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        // Проверка: товар должен быть опубликован
        if (product.getStatus() != Product.ProductStatus.PUBLISHED) {
            throw new InvalidStatusException("Можно пожаловаться только на опубликованные товары");
        }

        // Проверка: пользователь уже жаловался на этот товар?
        if (reportRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new DuplicateReportException("Вы уже жаловались на этот товар");
        }

        // Проверка: нельзя жаловаться на свой товар
        if (product.getFreelancer().getId().equals(userId)) {
            throw new ForbiddenException("Нельзя пожаловаться на свой товар");
        }

        // Создание жалобы
        ProductReport report = new ProductReport();
        report.setProduct(product);
        report.setUser(user);
        report.setReason(reason);
        report.setComment(comment);
        report.setIsReviewed(false);

        report = reportRepository.save(report);

        // Увеличение счётчика жалоб
        product.setReportsCount(product.getReportsCount() + 1);

        // Автоблокировка при 5+ жалобах
        if (product.getReportsCount() >= 5 && product.getStatus() == Product.ProductStatus.PUBLISHED) {
            product.setStatus(Product.ProductStatus.BLOCKED);
        }

        productRepository.save(product);

        return report;
    }

    /**
     * Получение всех жалоб на товар
     */
    public List<ProductReport> getReportsByProduct(Long productId) {
        return reportRepository.findByProductId(productId);
    }

    /**
     * Получение непроверенных жалоб (для админа)
     */
    public List<ProductReport> getUnreviewedReports() {
        return reportRepository.findByIsReviewedFalse();
    }

    /**
     * Получение жалоб на заблокированные товары
     */
    public List<ProductReport> getReportsForBlockedProducts() {
        return reportRepository.findUnreviewedForBlockedProducts();
    }

    /**
     * Пометка жалоб как проверенных
     */
    @Transactional
    public void markReportsAsReviewed(Long productId) {
        List<ProductReport> reports = reportRepository.findByProductId(productId);
        reports.forEach(report -> report.setIsReviewed(true));
        reportRepository.saveAll(reports);
    }

    /**
     * Получение количества жалоб на товар
     */
    public Long getReportsCount(Long productId) {
        return reportRepository.countByProductId(productId);
    }
}