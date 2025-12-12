package com.skillstorm.reliable_api.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Represents a single inventory record, tracking the quantity of a specific product
 * within a specific warehouse. This entity is the link between the {@code Warehouse} 
 * and {@code Product} master data, forming a many-to-many relationship (via an intermediary table).
 * The combination of {@code warehouse_id} and {@code product_id} is enforced as a composite unique key.
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
@Entity
@Table(
    name = "warehouse_inventory",
    uniqueConstraints = @UniqueConstraint(columnNames = {"warehouse_id", "product_id"})
)
public class WarehouseInventory {

    /**
     * The unique identifier (Primary Key) for the inventory record. Uses database identity generation.
     * Maps to the {@code inventory_id} column.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long id;

    /**
     * Many-to-One relationship to the {@code Warehouse} entity. This is the warehouse holding the inventory.
     * Maps to the {@code warehouse_id} foreign key column. It is part of the composite unique key.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    /**
     * Many-to-One relationship to the {@code Product} entity. This is the product being stored.
     * Maps to the {@code product_id} foreign key column. It is part of the composite unique key.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * The current quantity of the product stocked in the warehouse. Must be non-null.
     */
    @Column(nullable = false)
    private Integer quantity;

    /**
     * The expiration date of this batch of inventory, if applicable.
     */
    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    /**
     * The specific physical location within the warehouse where the product is stored (e.g., "Zone C").
     */
    @Column(name = "storage_location")
    private String storageLocation;

    /**
     * Timestamp of when the inventory record was created. Must be non-null and immutable.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp of the last time the inventory record (typically the quantity or location) was updated. Must be non-null.
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    
    /**
     * Default constructor required by JPA.
     */
    public WarehouseInventory() {}

    /**
     * Full parameterized constructor for creating a new inventory record.
     * * @param warehouse The warehouse entity holding the stock.
     * @param product The product entity being stocked.
     * @param quantity The initial quantity.
     * @param expirationDate The expiration date of the batch.
     * @param storageLocation The storage location in the warehouse.
     */
    public WarehouseInventory(Warehouse warehouse, Product product, Integer quantity,
                              LocalDate expirationDate, String storageLocation) {
        this.warehouse = warehouse;
        this.product = product;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
        this.storageLocation = storageLocation;
    }

    /**
     * Constructor for creating an inventory record without expiration date or specific storage location.
     * * @param warehouse The warehouse entity holding the stock.
     * @param product The product entity being stocked.
     * @param quantity The initial quantity.
     */
    public WarehouseInventory(Warehouse warehouse, Product product, Integer quantity) {
        this(warehouse, product, quantity, null, null);
    }

    // Getters and Setters
    
    /**
     * Retrieves the unique ID of the inventory record.
     * * @return The inventory ID.
     */
    public Long getId() { return id; }
    
    /**
     * Sets the unique ID of the inventory record.
     * * @param id The new inventory ID.
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Retrieves the associated warehouse entity.
     * * @return The warehouse.
     */
    public Warehouse getWarehouse() { return warehouse; }
    
    /**
     * Sets the associated warehouse entity.
     * * @param warehouse The new warehouse.
     */
    public void setWarehouse(Warehouse warehouse) { this.warehouse = warehouse; }

    /**
     * Retrieves the associated product entity.
     * * @return The product.
     */
    public Product getProduct() { return product; }
    
    /**
     * Sets the associated product entity.
     * * @param product The new product.
     */
    public void setProduct(Product product) { this.product = product; }

    /**
     * Retrieves the current stock quantity.
     * * @return The quantity.
     */
    public Integer getQuantity() { return quantity; }
    
    /**
     * Sets the current stock quantity.
     * * @param quantity The new quantity.
     */
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    /**
     * Retrieves the expiration date.
     * * @return The expiration date.
     */
    public LocalDate getExpirationDate() { return expirationDate; }
    
    /**
     * Sets the expiration date.
     * * @param expirationDate The new expiration date.
     */
    public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }

    /**
     * Retrieves the storage location string.
     * * @return The storage location.
     */
    public String getStorageLocation() { return storageLocation; }
    
    /**
     * Sets the storage location string.
     * * @param storageLocation The new storage location.
     */
    public void setStorageLocation(String storageLocation) { this.storageLocation = storageLocation; }

    /**
     * Retrieves the creation timestamp of the record.
     * * @return The creation timestamp.
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    /**
     * Sets the creation timestamp of the record.
     * * @param createdAt The new creation timestamp.
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    /**
     * Retrieves the last update timestamp of the record.
     * * @return The last update timestamp.
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    /**
     * Sets the last update timestamp of the record.
     * * @param updatedAt The new update timestamp.
     */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

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
     * Generates a string representation of the WarehouseInventory entity.
     * Includes the names of the associated warehouse and product for clarity.
     * * @return A string containing the key field values.
     */
    @Override
    public String toString() {
        return "WarehouseInventory [id=" + id + ", warehouse=" + warehouse.getName() +
               ", product=" + product.getName() + ", quantity=" + quantity +
               ", expirationDate=" + expirationDate + ", storageLocation=" + storageLocation + "]";
    }
}