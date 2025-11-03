package kz.handshop.exception;

import lombok.Getter;
import lombok.Setter;

// Товар не найден
@Getter
@Setter
public class ProductNotFoundException extends HandShopException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}