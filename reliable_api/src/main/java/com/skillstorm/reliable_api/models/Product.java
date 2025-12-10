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
@Table(name = "products")
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

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WarehouseInventory> warehouseInventory = new ArrayList<>();

    // Constructors
    public Product() {}

    public Product(String name, String sku, String description, Category category, String unit,
                   Boolean isHazardous, Boolean expirationRequired, BigDecimal price) {
        this.name = name;
        this.sku = sku;
        this.description = description;
        this.category = category;
        this.unit = unit;
        this.isHazardous = isHazardous;
        this.expirationRequired = expirationRequired;
        this.price = price;
        this.isDeleted = false;
    }

    // Getters & Setters
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getPublicId() { return publicId; }
    public void setPublicId(String publicId) { this.publicId = publicId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Boolean getIsHazardous() { return isHazardous; }
    public void setIsHazardous(Boolean isHazardous) { this.isHazardous = isHazardous; }

    public Boolean getExpirationRequired() { return expirationRequired; }
    public void setExpirationRequired(Boolean expirationRequired) { this.expirationRequired = expirationRequired; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public List<WarehouseInventory> getWarehouseInventory() { return warehouseInventory; }
    public void setWarehouseInventory(List<WarehouseInventory> warehouseInventory) { this.warehouseInventory = warehouseInventory; }

    // Lifecycle callbacks
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Product [productId=" + productId + ", publicId=" + publicId + ", name=" + name + 
               ", sku=" + sku + ", description=" + description + ", unit=" + unit + 
               ", isHazardous=" + isHazardous + ", expirationRequired=" + expirationRequired +
               ", price=" + price + ", isDeleted=" + isDeleted + ", category=" + 
               (category != null ? category.getName() : null) + "]";
    }
}
