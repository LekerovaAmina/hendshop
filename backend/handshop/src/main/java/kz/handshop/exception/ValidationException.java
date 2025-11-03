package kz.handshop.exception;

import lombok.Getter;
import lombok.Setter;

// Ошибка валидации
@Getter
@Setter
public class ValidationException extends HandShopException {
    public ValidationException(String message) {
        super(message);
    }
}