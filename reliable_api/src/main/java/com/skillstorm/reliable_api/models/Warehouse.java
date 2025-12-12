package com.skillstorm.reliable_api.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;

/**
 * Represents a warehouse entity that maps to the 'warehouses' table in the database.
 * This entity stores master data about a physical location and tracks its capacity and inventory.
 * {@code AuditingEntityListener} is used here, although the callbacks are explicitly defined.
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
@Entity
@Table(name = "warehouses")
@EntityListeners(AuditingEntityListener.class)
public class Warehouse {

    /**
     * The unique identifier (Primary Key) for the warehouse. Uses database identity generation.
     * Maps to the {@code warehouse_id} column.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * The name of the warehouse. Must be unique and non-null, with a maximum length of 150.
     */
    @Column(nullable = false, unique = true, length = 150)
    private String name;

    /**
     * The physical location or address of the warehouse. Must be non-null.
     */
    @Column(nullable = false)
    private String location;

    /**
     * The maximum total capacity (units/volume) the warehouse can hold. Must be non-null and at least 1.
     */
    @Min(value = 1, message = "Maximum capacity must be at least 1")
    @Column(name = "max_capacity", nullable = false)
    private int maxCapacity;

    /**
     * The current utilized capacity (units/volume) in the warehouse. Must be non-null and non-negative.
     */
    @Min(value = 0, message = "Current capacity cannot be negative")
    @Column(name = "current_capacity", nullable = false)
    private int currentCapacity;

    /**
     * Timestamp of when the warehouse record was created. Must be non-null and immutable.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp of the last time the warehouse record was updated. Must be non-null.
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * One-to-Many relationship with {@code WarehouseInventory}. Represents all products stocked
     * in this specific warehouse.
     * Cascade operations (ALL) and orphan removal are configured for dependency management.
     */
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WarehouseInventory> inventory = new ArrayList<>();

    // Constructors
    
    /**
     * Default constructor required by JPA.
     */
    public Warehouse() {}

    /**
     * Constructor for creating a new warehouse entity. Initializes {@code currentCapacity} to 0.
     * * @param name The name of the warehouse.
     * @param location The location/address.
     * @param maxCapacity The maximum storage capacity.
     */
    public Warehouse(String name, String location, int maxCapacity) {
        this.name = name;
        this.location = location;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = 0;
    }

    /**
     * Full parameterized constructor for creating a complete warehouse entity instance.
     * * @param warehouseId The unique ID.
     * @param name The name of the warehouse.
     * @param location The location/address.
     * @param maxCapacity The maximum storage capacity.
     * @param currentCapacity The currently used capacity.
     * @param createdAt The creation timestamp.
     * @param updatedAt The last updated timestamp.
     */
    public Warehouse(Long warehouseId, String name, String location, int maxCapacity, int currentCapacity,
                     LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.warehouseId = warehouseId;
        this.name = name;
        this.location = location;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = currentCapacity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters & Setters
    
    /**
     * Retrieves the unique ID of the warehouse.
     * * @return The warehouse ID.
     */
    public Long getWarehouseId() { return warehouseId; }
    
    /**
     * Sets the unique ID of the warehouse.
     * * @param warehouseId The new warehouse ID.
     */
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }

    /**
     * Retrieves the name of the warehouse.
     * * @return The warehouse name.
     */
    public String getName() { return name; }
    
    /**
     * Sets the name of the warehouse.
     * * @param name The new warehouse name.
     */
    public void setName(String name) { this.name = name; }

    /**
     * Retrieves the location of the warehouse.
     * * @return The warehouse location string.
     */
    public String getLocation() { return location; }
    
    /**
     * Sets the location of the warehouse.
     * * @param location The new warehouse location string.
     */
    public void setLocation(String location) { this.location = location; }

    /**
     * Retrieves the maximum capacity of the warehouse.
     * * @return The maximum capacity.
     */
    public int getMaxCapacity() { return maxCapacity; }
    
    /**
     * Sets the maximum capacity of the warehouse.
     * * @param maxCapacity The new maximum capacity.
     */
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    /**
     * Retrieves the current utilized capacity of the warehouse.
     * * @return The current capacity.
     */
    public int getCurrentCapacity() { return currentCapacity; }
    
    /**
     * Sets the current utilized capacity of the warehouse.
     * * @param currentCapacity The new current capacity.
     */
    public void setCurrentCapacity(int currentCapacity) { this.currentCapacity = currentCapacity; }

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

    /**
     * Retrieves the list of inventory records (products and stock) currently in this warehouse.
     * * @return The list of {@code WarehouseInventory} entities.
     */
    public List<WarehouseInventory> getInventory() { return inventory; }
    
    /**
     * Sets the list of inventory records for this warehouse.
     * * @param inventory The new list of {@code WarehouseInventory} entities.
     */
    public void setInventory(List<WarehouseInventory> inventory) { this.inventory = inventory; }

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
}