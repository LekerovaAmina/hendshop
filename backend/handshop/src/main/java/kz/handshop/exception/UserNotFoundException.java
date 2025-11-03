package kz.handshop.exception;

import lombok.Getter;
import lombok.Setter;

// Пользователь не найден
@Getter
@Setter
public class UserNotFoundException extends HandShopException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
