package com.skillstorm.reliable_api.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name="products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId; // internal DB ID

    @Column(nullable = false, unique = true, updatable = false)
    private String publicId = UUID.randomUUID().toString(); // exposed to API users

   @Column(nullable = false, length = 200)
private String name;

   @Column(nullable = false, unique = true, length = 100)
private String sku;

    @Column(columnDefinition = "TEXT")
    private String description;

   
    @Column(length = 50)
    private String unit;

    @Column(nullable = false)
    private Boolean isHazardous = false;

    @Column(nullable = false)
    private Boolean expirationRequired = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
      @Column(nullable = false)
    private BigDecimal price;
    @Column(name = "is_deleted", columnDefinition = "BOOLEAN DEFAULT FALSE")
private Boolean isDeleted = false;
 @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<WarehouseInventory> warehouseInventory = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

  

     public Product(String publicId, String name, String sku, String description, Category category, String unit,
            Boolean isHazardous, Boolean expirationRequired, BigDecimal price) {
        this.publicId = publicId;
        this.name = name;
        this.sku = sku;
        this.description = description;
        this.category = category;
        this.unit = unit;
        this.isHazardous = isHazardous;
        this.expirationRequired = expirationRequired;
        this.price = price;
    }

     public Product(String name, String sku, String description, Category category, String unit, Boolean isHazardous,
            Boolean expirationRequired, BigDecimal price) {
        this.name = name;
        this.sku = sku;
        this.description = description;
        this.category = category;
        this.unit = unit;
        this.isHazardous = isHazardous;
        this.expirationRequired = expirationRequired;
        this.price = price;
    }

     public Product() {
    }

     @Override
    public String toString() {
        return "Product [productId=" + productId + ", publicId=" + publicId + ", name=" + name + ", sku=" + sku
                + ", description=" + description + ", unit=" + unit + ", isHazardous=" + isHazardous
                + ", expirationRequired=" + expirationRequired + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
                + ", price=" + price + ", isDeleted=" + isDeleted + ", warehouseInventory=" + warehouseInventory
                + ", category=" + category + "]";
    }

     @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((productId == null) ? 0 : productId.hashCode());
        result = prime * result + ((publicId == null) ? 0 : publicId.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((sku == null) ? 0 : sku.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((unit == null) ? 0 : unit.hashCode());
        result = prime * result + ((isHazardous == null) ? 0 : isHazardous.hashCode());
        result = prime * result + ((expirationRequired == null) ? 0 : expirationRequired.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        result = prime * result + ((price == null) ? 0 : price.hashCode());
        result = prime * result + ((isDeleted == null) ? 0 : isDeleted.hashCode());
        result = prime * result + ((warehouseInventory == null) ? 0 : warehouseInventory.hashCode());
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        return result;
    }

     @Override
     public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Product other = (Product) obj;
        if (productId == null) {
            if (other.productId != null)
                return false;
        } else if (!productId.equals(other.productId))
            return false;
        if (publicId == null) {
            if (other.publicId != null)
                return false;
        } else if (!publicId.equals(other.publicId))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (sku == null) {
            if (other.sku != null)
                return false;
        } else if (!sku.equals(other.sku))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (unit == null) {
            if (other.unit != null)
                return false;
        } else if (!unit.equals(other.unit))
            return false;
        if (isHazardous == null) {
            if (other.isHazardous != null)
                return false;
        } else if (!isHazardous.equals(other.isHazardous))
            return false;
        if (expirationRequired == null) {
            if (other.expirationRequired != null)
                return false;
        } else if (!expirationRequired.equals(other.expirationRequired))
            return false;
        if (createdAt == null) {
            if (other.createdAt != null)
                return false;
        } else if (!createdAt.equals(other.createdAt))
            return false;
        if (updatedAt == null) {
            if (other.updatedAt != null)
                return false;
        } else if (!updatedAt.equals(other.updatedAt))
            return false;
        if (price == null) {
            if (other.price != null)
                return false;
        } else if (!price.equals(other.price))
            return false;
        if (isDeleted == null) {
            if (other.isDeleted != null)
                return false;
        } else if (!isDeleted.equals(other.isDeleted))
            return false;
        if (warehouseInventory == null) {
            if (other.warehouseInventory != null)
                return false;
        } else if (!warehouseInventory.equals(other.warehouseInventory))
            return false;
        if (category == null) {
            if (other.category != null)
                return false;
        } else if (!category.equals(other.category))
            return false;
        return true;
     }

     @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public List<WarehouseInventory> getWarehouseInventory() {
        return warehouseInventory;
    }

    public void setWarehouseInventory(List<WarehouseInventory> warehouseInventory) {
        this.warehouseInventory = warehouseInventory;
    }
}
