package kz.handshop.exception;

import lombok.Getter;
import lombok.Setter;

// Доступ запрещён
@Getter
@Setter
public class ForbiddenException extends HandShopException {
    public ForbiddenException(String message) {
        super(message);
    }
}
