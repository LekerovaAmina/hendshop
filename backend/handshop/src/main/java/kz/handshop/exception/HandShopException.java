package kz.handshop.exception;

public class HandShopException extends RuntimeException {
    public HandShopException(String message) {
        super(message);
    }

    public HandShopException(String message, Throwable cause) {
        super(message, cause);
    }
}
