package kz.handshop.dto.request;

import jakarta.validation.constraints.NotBlank;

public class RejectProductRequest {

    @NotBlank(message = "Комментарий обязателен")
    private String comment;

    public RejectProductRequest() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}