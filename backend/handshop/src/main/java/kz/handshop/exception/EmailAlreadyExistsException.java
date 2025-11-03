package kz.handshop.exception;

import lombok.Getter;
import lombok.Setter;

// Email уже существует
@Getter
@Setter
public class EmailAlreadyExistsException extends HandShopException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
