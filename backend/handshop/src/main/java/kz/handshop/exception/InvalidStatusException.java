package kz.handshop.exception;

import lombok.Getter;
import lombok.Setter;

// Недопустимый статус для операции
@Getter
@Setter
public class InvalidStatusException extends HandShopException {
    public InvalidStatusException(String message) {
        super(message);
    }
}