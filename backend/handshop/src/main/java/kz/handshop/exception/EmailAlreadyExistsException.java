package kz.handshop.exception;

public class EmailAlreadyExistsException extends HandShopException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}