package com.skillstorm.reliable_api.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) used for creating a new inventory record for a product
 * in a specific warehouse (i.e., stocking a product for the first time).
 * This DTO is critical for the initial {@code POST /warehouses/{warehouseId}/inventory} endpoint.
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
public class WarehouseInventoryCreateDTO {

    /**
     * The unique public ID of the product being stocked. Must not be blank.
     */
    @NotBlank
    private String productPublicId;

    /**
     * The initial quantity of the product being added to the warehouse. Must not be null and must be at least 1.
     */
    @NotNull
    @Min(1)
    private Integer quantity;

    /**
     * The specific storage location within the warehouse where the product is placed (e.g., "Aisle 3, Shelf B").
     * This field is optional.
     */
    private String storageLocation;
    
    /**
     * The expiration date of the current inventory batch. This is required if the product 
     * metadata specifies {@code expirationRequired = true}.
     */
    private LocalDate expirationDate;

    // Getters and Setters

    /**
     * Provides accessor and mutator methods for all fields:
     * (productPublicId, quantity, storageLocation, expirationDate).
     * <p>
     * {@code getProductPublicId()}, {@code setProductPublicId(String productPublicId)}
     * <p>
     * {@code getQuantity()}, {@code setQuantity(Integer quantity)}
     * <p>
     * {@code getStorageLocation()}, {@code setStorageLocation(String storageLocation)}
     * <p>
     * {@code getExpirationDate()}, {@code setExpirationDate(LocalDate expirationDate)}
     * </p>
     */
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