package com.skillstorm.reliable_api.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) representing a single inventory record (a product stocked in a warehouse).
 * This class is used for read operations, providing detailed stock information, including 
 * nested product metadata via {@code ProductDTO}.
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
public class WarehouseInventoryDTO {

    /**
     * The unique public ID of the product currently stocked. Must not be blank.
     */
    @NotBlank
    private String productPublicId;

    /**
     * The quantity of this product currently in stock. Must not be null and must be at least 1.
     */
    @NotNull
    @Min(1)
    private Integer quantity;

    /**
     * The specific storage location within the warehouse (e.g., "Aisle 3, Shelf B").
     */
    private String storageLocation;
    
    /**
     * The expiration date of the current inventory batch, if required by the product.
     */
    private LocalDate expirationDate;

    /**
     * The detailed metadata for the product associated with this inventory record.
     */
    private ProductDTO product; 

    // Getters and Setters
    
    /**
     * Provides accessor and mutator methods for all fields:
     * (productPublicId, quantity, storageLocation, expirationDate, product).
     * <p>
     * {@code getProductPublicId()}, {@code setProductPublicId(String productPublicId)}
     * <p>
     * {@code getQuantity()}, {@code setQuantity(Integer quantity)}
     * <p>
     * {@code getStorageLocation()}, {@code setStorageLocation(String storageLocation)}
     * <p>
     * {@code getExpirationDate()}, {@code setExpirationDate(LocalDate expirationDate)}
     * <p>
     * {@code getProduct()}, {@code setProduct(ProductDTO product)}
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

    public ProductDTO getProduct() {
        return product;
    }
    public void setProduct(ProductDTO product) {
        this.product = product;
    }
}