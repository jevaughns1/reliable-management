package com.skillstorm.reliable_api.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class WarehouseInventoryCreateDTO {

    @NotBlank
    private String productPublicId;

    @NotNull
    @Min(1)
    private Integer quantity;

    private String storageLocation;
    private LocalDate expirationDate;

    public String getProductPublicId() {
        return productPublicId;
    }
    public void setProductPublicId(String productPublicId) {
        this.productPublicId = productPublicId;
    }

    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getStorageLocation() {
        return storageLocation;
    }
    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }
    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }
}
