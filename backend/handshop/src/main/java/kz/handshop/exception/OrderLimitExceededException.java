package kz.handshop.exception;

public class OrderLimitExceededException extends HandShopException {
    public OrderLimitExceededException(String message) {
        super(message);
    }
}