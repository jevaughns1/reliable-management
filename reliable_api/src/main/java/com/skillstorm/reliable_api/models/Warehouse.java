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

@Entity
@Table(name = "warehouses")
@EntityListeners(AuditingEntityListener.class)
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(nullable = false, unique = true, length = 150)
    private String name;

    @Column(nullable = false)
    private String location;

    @Min(value = 1, message = "Maximum capacity must be at least 1")
    @Column(name = "max_capacity", nullable = false)
    private int maxCapacity;

    @Min(value = 0, message = "Current capacity cannot be negative")
    @Column(name = "current_capacity", nullable = false)
    private int currentCapacity;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WarehouseInventory> inventory = new ArrayList<>();

    // Constructors
    public Warehouse() {}

    public Warehouse(String name, String location, int maxCapacity) {
        this.name = name;
        this.location = location;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = 0;
    }

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
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    public int getCurrentCapacity() { return currentCapacity; }
    public void setCurrentCapacity(int currentCapacity) { this.currentCapacity = currentCapacity; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<WarehouseInventory> getInventory() { return inventory; }
    public void setInventory(List<WarehouseInventory> inventory) { this.inventory = inventory; }

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

    
}
