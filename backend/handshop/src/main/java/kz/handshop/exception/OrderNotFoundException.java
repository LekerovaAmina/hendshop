package kz.handshop.exception;

public class OrderNotFoundException extends HandShopException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}