package com.skillstorm.reliable_api.dtos;




import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductUpdateDTO {
      @Size(max = 200)
    @NotBlank 
    private String name;
    @Size(max = 100)
    @NotBlank
    private String sku;
    @NotNull
    private String description;
    @NotNull
    private String publicId;
    @NotNull
    private Long categoryId;
    @NotBlank
    private String unit;
    @NotNull
    private Boolean isHazardous;
    @NotNull
    private Boolean expirationRequired;
    @NotNull
    private  BigDecimal price;
    
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
    public Boolean getExpirationRequired() {
        return expirationRequired;
    }
    public void setExpirationRequired(Boolean expirationRequired) {
        this.expirationRequired = expirationRequired;
    }
    
}
