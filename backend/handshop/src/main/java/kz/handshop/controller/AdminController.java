package kz.handshop.controller;

import kz.handshop.dto.request.RejectProductRequest;
import kz.handshop.dto.response.ProductResponse;
import kz.handshop.dto.response.ReportResponse;
import kz.handshop.model.Product;
import kz.handshop.model.ProductReport;
import kz.handshop.repository.ProductRepository;
import kz.handshop.repository.ProductReportRepository;
import kz.handshop.service.ProductReportService;
import kz.handshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService productService;
    private final ProductReportService reportService;
    private final ProductRepository productRepository;
    private final ProductReportRepository reportRepository;

    // ==================== МОДЕРАЦИЯ ТОВАРОВ ====================

    /**
     * Получить товары на модерации
     * GET /api/admin/products/moderation
     */
    @GetMapping("/products/moderation")
    public ResponseEntity<List<ProductResponse>> getProductsForModeration() {
        List<Product> products = productRepository.findPendingModeration();

        List<ProductResponse> response = products.stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Одобрить товар
     * PATCH /api/admin/products/{id}/approve
     */
    @PatchMapping("/products/{id}/approve")
    public ResponseEntity<ProductResponse> approveProduct(
            @PathVariable Long id,
            @RequestParam Long adminId) {

        Product product = productService.approveProduct(id, adminId);

        return ResponseEntity.ok(toProductResponse(product));
    }

    /**
     * Отклонить товар
     * PATCH /api/admin/products/{id}/reject
     */
    @PatchMapping("/products/{id}/reject")
    public ResponseEntity<ProductResponse> rejectProduct(
            @PathVariable Long id,
            @Valid @RequestBody RejectProductRequest request,
            @RequestParam Long adminId) {

        Product product = productService.rejectProduct(id, adminId, request.getComment());

        return ResponseEntity.ok(toProductResponse(product));
    }

    // ==================== ЖАЛОБЫ ====================

    /**
     * Получить все непроверенные жалобы
     * GET /api/admin/reports
     */
    @GetMapping("/reports")
    public ResponseEntity<List<ReportResponse>> getUnreviewedReports() {
        List<ProductReport> reports = reportService.getUnreviewedReports();

        List<ReportResponse> response = reports.stream()
                .map(this::toReportResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Получить жалобы на заблокированные товары
     * GET /api/admin/reports/blocked
     */
    @GetMapping("/reports/blocked")
    public ResponseEntity<List<ReportResponse>> getBlockedProductReports() {
        List<ProductReport> reports = reportService.getReportsForBlockedProducts();

        List<ReportResponse> response = reports.stream()
                .map(this::toReportResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Разблокировать товар (жалобы необоснованны)
     * PATCH /api/admin/reports/{productId}/reject
     */
    @PatchMapping("/reports/{productId}/reject")
    public ResponseEntity<Map<String, String>> rejectReports(
            @PathVariable Long productId,
            @RequestParam Long adminId) {

        Product product = productService.unblockProduct(productId, adminId);

        return ResponseEntity.ok(Map.of(
                "message", "Товар разблокирован, жалобы отклонены",
                "productId", productId.toString(),
                "newStatus", product.getStatus().name()
        ));
    }

    /**
     * Удалить товар (жалобы обоснованны)
     * PATCH /api/admin/reports/{productId}/accept
     */
    @PatchMapping("/reports/{productId}/accept")
    public ResponseEntity<Map<String, String>> acceptReports(
            @PathVariable Long productId,
            @RequestParam Long adminId) {

        Product product = productService.deleteProduct(productId, adminId);
        reportService.markReportsAsReviewed(productId);

        return ResponseEntity.ok(Map.of(
                "message", "Товар удалён",
                "productId", productId.toString(),
                "status", product.getStatus().name()
        ));
    }

    // ==================== HELPER METHODS ====================

    private ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getMaterials(),
                product.getPrice(),
                product.getProductionTime(),
                product.getDeliveryType().name(),
                product.getStatus().name(),
                product.getPrimaryImageUrl(),
                product.getViewsCount(),
                product.getAverageRating(),
                product.getReviewsCount(),
                product.getFreelancer().getId(),
                product.getFreelancer().getUsername()
        );
    }

    private ReportResponse toReportResponse(ProductReport report) {
        return new ReportResponse(
                report.getId(),
                report.getProduct().getId(),
                report.getProduct().getTitle(),
                report.getUser().getId(),
                report.getUser().getUsername(),
                report.getReason().name(),
                report.getComment(),
                report.getCreatedAt(),
                report.getIsReviewed()
        );
    }
}