package com.skillstorm.reliable_api.dtos;


import java.math.BigDecimal;

import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) used for partially updating an existing product (PATCH request).
 * This class contains optional fields, allowing specific properties of a product 
 * to be modified without requiring the full product data, leveraging the product's 
 * unique {@code publicId} for identification (which is typically handled by the controller).
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
public class ProductPatchDTO {
    
    /**
     * The updated name of the product. Optional for a patch operation.
     * Has a maximum size of 200 characters.
     */
    @Size(max = 200)
    private String name;
    
    /**
     * The updated Stock Keeping Unit (SKU) of the product. Optional for a patch operation.
     * Has a maximum size of 100 characters.
     */
    @Size(max = 100)
    private String sku;
    
    /**
     * The updated description of the product. Optional for a patch operation.
     */
    private String description;
    
    /**
     * The public, universally unique identifier (UUID) for the product.
     * While technically part of the DTO, this is typically used by the service layer
     * for lookup and not intended for modification during a patch.
     */
    private String publicId;
    
    /**
     * The updated selling price of the product. Optional for a patch operation.
     */
    private BigDecimal price;
    
    /**
     * The updated foreign key ID linking to the product's {@code Category}. Optional.
     */
    private Long categoryId;
    
    /**
     * The updated unit of measure for this product. Optional for a patch operation.
     */
    private String unit;
    
    /**
     * Flag indicating if the product is classified as a hazardous material. Optional.
     */
    private Boolean isHazardous;
    
    // Getters and Setters

    /**
     * Provides accessor and mutator methods for all product fields:
     * (name, sku, description, publicId, price, categoryId, unit, isHazardous).
     * <p>
     * {@code getName()}, {@code setName(String name)}
     * <p>
     * {@code getSku()}, {@code setSku(String sku)}
     * <p>
     * {@code getDescription()}, {@code setDescription(String description)}
     * <p>
     * {@code getPublicId()}, {@code setPublicId(String publicId)}
     * <p>
     * {@code getPrice()}, {@code setPrice(BigDecimal price)}
     * <p>
     * {@code getCategoryId()}, {@code setCategoryId(Long categoryId)}
     * <p>
     * {@code getUnit()}, {@code setUnit(String unit)}
     * <p>
     * {@code getIsHazardous()}, {@code setIsHazardous(Boolean isHazardous)}
     * </p>
     */
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
    public String getPublicId() {
        return publicId;
    }
    public void setPublicId(String publicId) {
        this.publicId = publicId;
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
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}