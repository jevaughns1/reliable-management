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

/**
 * Represents a product master data entity that maps to the 'products' table in the database.
 * This entity stores fundamental information about a tradable item and manages relationships
 * with its category and inventory records across warehouses.
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
@Entity
@Table(name = "products")
public class Product {

    /**
     * The unique internal database identifier (Primary Key). Uses identity generation.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId; // internal DB ID

    /**
     * A universally unique identifier (UUID) exposed to external API users for resource lookup.
     * This field is generated automatically, is unique, and immutable (cannot be updated).
     */
    @Column(nullable = false, unique = true, updatable = false)
    private String publicId = UUID.randomUUID().toString(); // exposed to API users

    /**
     * The name of the product. Must be non-null, with a maximum length of 200.
     */
    @Column(nullable = false, length = 200)
    private String name;

    /**
     * The Stock Keeping Unit (SKU) of the product. Must be unique and non-null, with a maximum length of 100.
     */
    @Column(nullable = false, unique = true, length = 100)
    private String sku;

    /**
     * A detailed description of the product. Mapped to a TEXT column for longer content.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * The unit of measure (e.g., 'EA', 'BOX', 'KG').
     */
    @Column(length = 50)
    private String unit;

    /**
     * Flag indicating if the product is classified as a hazardous material. Defaults to false.
     * Must be non-null.
     */
    @Column(nullable = false)
    private Boolean isHazardous = false;

    /**
     * Flag indicating if individual inventory batches of this product must track an expiration date.
     * Defaults to false. Must be non-null.
     */
    @Column(nullable = false)
    private Boolean expirationRequired = false;

    /**
     * The selling price of the product. Must be non-null.
     */
    @Column(nullable = false)
    private BigDecimal price;

    /**
     * Flag indicating if the product has been logically deleted (soft delete). Defaults to false.
     */
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    /**
     * Timestamp of when the product record was created. Must be non-null and immutable.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp of the last time the product record was updated. Must be non-null.
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Many-to-One relationship to the {@code Category} entity. Maps to the {@code category_id} foreign key column.
     * Fetch type is lazy.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * One-to-Many relationship with {@code WarehouseInventory}. Represents all stock records
     * for this product across all warehouses.
     * Cascade operations (ALL) and orphan removal are configured for dependency management.
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WarehouseInventory> warehouseInventory = new ArrayList<>();

    // Constructors
    
    /**
     * Default constructor required by JPA.
     */
    public Product() {}

    /**
     * Parameterized constructor for creating a new product entity.
     * * @param name The product name.
     * @param sku The product SKU.
     * @param description The product description.
     * @param category The category the product belongs to.
     * @param unit The unit of measure.
     * @param isHazardous Flag for hazardous material status.
     * @param expirationRequired Flag for expiration tracking requirement.
     * @param price The selling price.
     */
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
    
    /**
     * Retrieves the internal database ID of the product.
     * * @return The product ID.
     */
    public Long getProductId() { return productId; }
    
    /**
     * Sets the internal database ID of the product.
     * * @param productId The new product ID.
     */
    public void setProductId(Long productId) { this.productId = productId; }

    /**
     * Retrieves the public unique identifier of the product.
     * * @return The public ID (UUID string).
     */
    public String getPublicId() { return publicId; }
    
    /**
     * Sets the public unique identifier of the product.
     * * @param publicId The new public ID.
     */
    public void setPublicId(String publicId) { this.publicId = publicId; }

    /**
     * Retrieves the name of the product.
     * * @return The product name.
     */
    public String getName() { return name; }
    
    /**
     * Sets the name of the product.
     * * @param name The new product name.
     */
    public void setName(String name) { this.name = name; }

    /**
     * Retrieves the SKU of the product.
     * * @return The product SKU.
     */
    public String getSku() { return sku; }
    
    /**
     * Sets the SKU of the product.
     * * @param sku The new product SKU.
     */
    public void setSku(String sku) { this.sku = sku; }

    /**
     * Retrieves the description of the product.
     * * @return The product description.
     */
    public String getDescription() { return description; }
    
    /**
     * Sets the description of the product.
     * * @param description The new product description.
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Retrieves the unit of measure.
     * * @return The unit of measure string.
     */
    public String getUnit() { return unit; }
    
    /**
     * Sets the unit of measure.
     * * @param unit The new unit of measure string.
     */
    public void setUnit(String unit) { this.unit = unit; }

    /**
     * Retrieves the hazardous status flag.
     * * @return {@code true} if hazardous, {@code false} otherwise.
     */
    public Boolean getIsHazardous() { return isHazardous; }
    
    /**
     * Sets the hazardous status flag.
     * * @param isHazardous The new hazardous status.
     */
    public void setIsHazardous(Boolean isHazardous) { this.isHazardous = isHazardous; }

    /**
     * Retrieves the expiration required flag.
     * * @return {@code true} if expiration tracking is required, {@code false} otherwise.
     */
    public Boolean getExpirationRequired() { return expirationRequired; }
    
    /**
     * Sets the expiration required flag.
     * * @param expirationRequired The new expiration required status.
     */
    public void setExpirationRequired(Boolean expirationRequired) { this.expirationRequired = expirationRequired; }

    /**
     * Retrieves the selling price of the product.
     * * @return The price as a {@code BigDecimal}.
     */
    public BigDecimal getPrice() { return price; }
    
    /**
     * Sets the selling price of the product.
     * * @param price The new price.
     */
    public void setPrice(BigDecimal price) { this.price = price; }

    /**
     * Retrieves the soft-delete status flag.
     * * @return {@code true} if logically deleted, {@code false} otherwise.
     */
    public Boolean getIsDeleted() { return isDeleted; }
    
    /**
     * Sets the soft-delete status flag.
     * * @param isDeleted The new soft-delete status.
     */
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }

    /**
     * Retrieves the category of the product.
     * * @return The associated {@code Category} entity.
     */
    public Category getCategory() { return category; }
    
    /**
     * Sets the category of the product.
     * * @param category The new {@code Category} entity.
     */
    public void setCategory(Category category) { this.category = category; }

    /**
     * Retrieves the list of inventory records for this product across all warehouses.
     * * @return The list of {@code WarehouseInventory} entities.
     */
    public List<WarehouseInventory> getWarehouseInventory() { return warehouseInventory; }
    
    /**
     * Sets the list of inventory records for this product.
     * * @param warehouseInventory The new list of {@code WarehouseInventory} entities.
     */
    public void setWarehouseInventory(List<WarehouseInventory> warehouseInventory) { this.warehouseInventory = warehouseInventory; }

    // Lifecycle callbacks
    
    /**
     * JPA pre-persist callback method.
     * Initializes the {@code createdAt} and {@code updatedAt} fields to the current time 
     * immediately before the entity is first persisted (inserted) into the database.
     */
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    /**
     * JPA pre-update callback method.
     * Updates the {@code updatedAt} field to the current time immediately before 
     * the entity is updated (merged) in the database.
     */
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Generates a string representation of the Product entity, including the category name if available.
     * * @return A string containing the field values.
     */
    @Override
    public String toString() {
        return "Product [productId=" + productId + ", publicId=" + publicId + ", name=" + name + 
               ", sku=" + sku + ", description=" + description + ", unit=" + unit + 
               ", isHazardous=" + isHazardous + ", expirationRequired=" + expirationRequired +
               ", price=" + price + ", isDeleted=" + isDeleted + ", category=" + 
               (category != null ? category.getName() : null) + "]";
    }

    /**
     * Retrieves the creation timestamp of the record.
     * * @return The creation timestamp.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp of the record. (Primarily for internal/testing use).
     * * @param createdAt The new creation timestamp.
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Retrieves the last update timestamp of the record.
     * * @return The last update timestamp.
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the last update timestamp of the record. (Primarily for internal/testing use).
     * * @param updatedAt The new update timestamp.
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}