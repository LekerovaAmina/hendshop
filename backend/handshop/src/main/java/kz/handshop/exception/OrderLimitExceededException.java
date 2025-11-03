package kz.handshop.exception;

import lombok.Getter;
import lombok.Setter;

// Превышен лимит заказов
@Getter
@Setter
public class OrderLimitExceededException extends HandShopException {
    public OrderLimitExceededException(String message) {
        super(message);
    }
}