package com.skillstorm.reliable_api.dtos;


import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) used for fully updating an existing product (PUT request).
 * This class enforces strict validation, requiring all essential product attributes
 * to be provided, as it represents a complete replacement of the product resource's
 * editable fields.
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
public class ProductUpdateDTO {
    
    /**
     * The new, updated name of the product. Must not be blank and has a maximum size of 200 characters.
     */
    @Size(max = 200)
    @NotBlank 
    private String name;
    
    /**
     * The new, updated Stock Keeping Unit (SKU) of the product. Must not be blank and 
     * has a maximum size of 100 characters.
     */
    @Size(max = 100)
    @NotBlank
    private String sku;
    
    /**
     * The new, updated description of the product. Must not be null.
     */
    @NotNull
    private String description;
    
    /**
     * The new, updated foreign key ID linking to the product's {@code Category}. Must not be null.
     */
    @NotNull
    private Long categoryId;
    
    /**
     * The new, updated unit of measure for this product. Must not be blank.
     */
    @NotBlank
    private String unit;
    
    /**
     * The new regulatory flag indicating if the product is classified as a hazardous material. Must not be null.
     */
    @NotNull
    private Boolean isHazardous;
    
    /**
     * The new flag indicating if individual inventory batches of this product must track an expiration date. Must not be null.
     */
    @NotNull
    private Boolean expirationRequired;
    
    /**
     * The new, updated selling price of the product. Must not be null.
     */
    @NotNull
    private  BigDecimal price;
    
    // Getters and Setters

    /**
     * Provides accessor and mutator methods for all mandatory fields:
     * (name, sku, description, categoryId, unit, isHazardous, expirationRequired, price).
     * <p>
     * {@code getName()}, {@code setName(String name)}
     * <p>
     * {@code getSku()}, {@code setSku(String sku)}
     * <p>
     * {@code getDescription()}, {@code setDescription(String description)}
     * <p>
     * {@code getCategoryId()}, {@code setCategoryId(Long categoryId)}
     * <p>
     * {@code getUnit()}, {@code setUnit(String unit)}
     * <p>
     * {@code getIsHazardous()}, {@code setIsHazardous(Boolean isHazardous)}
     * <p>
     * {@code getExpirationRequired()}, {@code setExpirationRequired(Boolean expirationRequired)}
     * <p>
     * {@code getPrice()}, {@code setPrice(BigDecimal price)}
     * </p>
     */
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSku() {
        return sku;
    }
    public void setSku(String sku) {
        this.sku = sku;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public Boolean getIsHazardous() {
        return isHazardous;
    }
    public void setIsHazardous(Boolean isHazardous) {
        this.isHazardous = isHazardous;
    }
    public Boolean getExpirationRequired() {
        return expirationRequired;
    }
    public void setExpirationRequired(Boolean expirationRequired) {
        this.expirationRequired = expirationRequired;
    }
    
}