package kz.handshop.entity;

public enum ProductStatus {
    DRAFT,              // Черновик
    MODERATION,         // На модерации
    EDIT_MODERATION,    // Отправлен на доработку
    PUBLISHED,          // Опубликован
    ARCHIVED,           // В архиве
    BLOCKED,            // Заблокирован (5+ жалоб)
    DELETED             // Удалён админом
}