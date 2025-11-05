package kz.handshop.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ReportProductRequest {

    @NotBlank(message = "Причина жалобы обязательна")
    private String reason;

    private String comment;

    public ReportProductRequest() {
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}