package kz.handshop.exception;

import lombok.Getter;
import lombok.Setter;

// Полка не найдена
@Getter
@Setter
public class ShelfNotFoundException extends HandShopException {
    public ShelfNotFoundException(String message) {
        super(message);
    }
}