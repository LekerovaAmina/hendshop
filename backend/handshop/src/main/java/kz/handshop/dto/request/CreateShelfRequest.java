package kz.handshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateShelfRequest {

    @NotNull(message = "ID категории обязателен")
    private Long globalCategoryId;

    @NotBlank(message = "Название полки обязательно")
    private String shelfName;

    private String description;

    public CreateShelfRequest() {
    }

    public Long getGlobalCategoryId() {
        return globalCategoryId;
    }

    public void setGlobalCategoryId(Long globalCategoryId) {
        this.globalCategoryId = globalCategoryId;
    }

    public String getShelfName() {
        return shelfName;
    }

    public void setShelfName(String shelfName) {
        this.shelfName = shelfName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}