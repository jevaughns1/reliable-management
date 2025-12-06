package com.skillstorm.reliable_api.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    @Column(name="warehouse_id")
    private Long warehouseId; 
    @Column(name = "name", length = 150, nullable = false, unique = true)
    private String name;
    @Column(name = "location", nullable = false)
    private String location;
    @Min(value = 1, message = "Maximum capacity must be at least 1")
    @Column(name = "max_capacity", nullable = false)
    private int maxCapacity;
    @Min(value = 0, message = "Current capacity cannot be negative")
    @Column(name = "current_capacity", nullable = false)
    private int currentCapacity;
    @CreatedDate // created on first save
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate // updates on every subsequent save/update
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    
    //CONSTRUCTORS
    public Warehouse() {
    }
    public Warehouse(Long warehouseId, String name, String location, int maxCapacity,
            @Min(value = 0, message = "Current capacity cannot be negative") int currentCapacity,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.warehouseId = warehouseId;
        this.name = name;
        this.location = location;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = currentCapacity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
   public Warehouse(String name, String location, int maxCapacity) {
    this.name = name;
    this.location = location;
    this.maxCapacity = maxCapacity;
    this.currentCapacity = 0;
}
   
// GETTERS/SETTERS
    public Long getWarehouseId() {
        return warehouseId;
    }
    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public int getMaxCapacity() {
        return maxCapacity;
    }
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
    public int getCurrentCapacity() {
        return currentCapacity;
    }
    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
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
      
    

     @Override
    public String toString() {
        return "Warehouse [warehouseId=" + warehouseId + ", name=" + name + ", location=" + location + ", maxCapacity="
                + maxCapacity + ", currentCapacity=" + currentCapacity + ", createdAt=" + createdAt + ", updatedAt="
                + updatedAt + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((warehouseId == null) ? 0 : warehouseId.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + maxCapacity;
        result = prime * result + currentCapacity;
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
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
        Warehouse other = (Warehouse) obj;
        if (warehouseId == null) {
            if (other.warehouseId != null)
                return false;
        } else if (!warehouseId.equals(other.warehouseId))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
            return false;
        if (maxCapacity != other.maxCapacity)
            return false;
        if (currentCapacity != other.currentCapacity)
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
        return true;
    }

         // JPA lifecycle callbacks
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
