package kz.handshop.exception;

import lombok.Getter;
import lombok.Setter;

// Базовое исключение приложения
@Getter
@Setter
public class HandShopException extends RuntimeException {
    public HandShopException(String message) {
        super(message);
    }

    public HandShopException(String message, Throwable cause) {
        super(message, cause);
    }
}
