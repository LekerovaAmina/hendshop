package kz.handshop.exception;

import lombok.Getter;
import lombok.Setter;

// Заказ не найден
@Getter
@Setter
public class OrderNotFoundException extends HandShopException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
