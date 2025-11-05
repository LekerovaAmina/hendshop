package kz.handshop.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CreateDeliveryAddressRequest {

    @NotBlank(message = "Город обязателен")
    private String city;

    @NotBlank(message = "Улица обязательна")
    private String street;

    private String building;
    private String apartment;
    private String postalCode;
    private Boolean isDefault;

    public CreateDeliveryAddressRequest() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }
}