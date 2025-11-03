package kz.handshop.exception;

import lombok.Getter;
import lombok.Setter;

// Категория не найдена
@Getter
@Setter
public class CategoryNotFoundException extends HandShopException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
