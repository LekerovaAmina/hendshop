package kz.handshop.exception;

import lombok.Getter;
import lombok.Setter;

// Дублирование жалобы
@Getter
@Setter
public class DuplicateReportException extends HandShopException {
    public DuplicateReportException(String message) {
        super(message);
    }
}
