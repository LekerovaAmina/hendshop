package kz.handshop.dto.request;

import jakarta.validation.constraints.NotBlank;

public class UpgradeToFreelancerRequest {

    @NotBlank(message = "Название магазина обязательно")
    private String shopName;

    private String shopDescription;

    public UpgradeToFreelancerRequest() {
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopDescription() {
        return shopDescription;
    }

    public void setShopDescription(String shopDescription) {
        this.shopDescription = shopDescription;
    }
}