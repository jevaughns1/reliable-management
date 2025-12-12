package com.skillstorm.reliable_api.models;

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
import jakarta.persistence.Table;

/**
 * Represents an entity for tracking the transfer of a product quantity between two warehouses.
 * This entity maps to the 'inventory_transfers' table and records transactional history.
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
@Entity
@Table(name = "inventory_transfers")
public class InventoryTransfer {

    /**
     * Default constructor required by JPA.
     */
    public InventoryTransfer() {
    }

    /**
     * Constructor for creating a basic inventory transfer record.
     * * @param product The product being transferred.
     * @param sourceWarehouse The warehouse the product is moving from.
     * @param destinationWarehouse The warehouse the product is moving to.
     * @param quantity The quantity being moved.
     */
    public InventoryTransfer(Product product, Warehouse sourceWarehouse, Warehouse destinationWarehouse,
            Integer quantity) {
        this.product = product;
        this.sourceWarehouse = sourceWarehouse;
        this.destinationWarehouse = destinationWarehouse;
        this.quantity = quantity;
    }

    /**
     * Full parameterized constructor for creating a complete inventory transfer record.
     * * @param transferId The unique ID of the transfer record.
     * @param product The product being transferred.
     * @param sourceWarehouse The warehouse the product is moving from.
     * @param destinationWarehouse The warehouse the product is moving to.
     * @param quantity The quantity being moved.
     * @param transferNotes Optional notes regarding the transfer.
     * @param createdAt The timestamp when the record was created.
     */
    public InventoryTransfer(Long transferId, Product product, Warehouse sourceWarehouse,
            Warehouse destinationWarehouse, Integer quantity, String transferNotes, LocalDateTime createdAt) {
        this.transferId = transferId;
        this.product = product;
        this.sourceWarehouse = sourceWarehouse;
        this.destinationWarehouse = destinationWarehouse;
        this.quantity = quantity;
        this.transferNotes = transferNotes;
        this.createdAt = createdAt;
    }

    /**
     * The unique identifier (Primary Key) for the inventory transfer record.
     * Uses database identity generation strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transferId;

    /**
     * The product associated with this transfer. Establishes a Many-to-One relationship 
     * with the {@code Product} entity. Maps to the {@code product_id} foreign key column.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * The source warehouse for the transfer. Establishes a Many-to-One relationship 
     * with the {@code Warehouse} entity. Maps to the {@code source_warehouse_id} foreign key column.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_warehouse_id")
    private Warehouse sourceWarehouse;

    /**
     * The destination warehouse for the transfer. Establishes a Many-to-One relationship 
     * with the {@code Warehouse} entity. Maps to the {@code destination_warehouse_id} foreign key column.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_warehouse_id")
    private Warehouse destinationWarehouse;

    /**
     * The quantity of the product that was transferred. Must not be null.
     */
    @Column(nullable = false)
    private Integer quantity;

    /**
     * Optional notes or justification pertaining to the transfer.
     */
    private String transferNotes;

    /**
     * Timestamp indicating when the transfer record was created.
     * This field is non-null and should not be updated after persistence.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Generates a string representation of the InventoryTransfer entity.
     * * @return A string containing the field values.
     */
    @Override
    public String toString() {
        return "InventoryTransfer [transferId=" + transferId + ", product=" + product + ", sourceWarehouse="
                + sourceWarehouse + ", destinationWarehouse=" + destinationWarehouse + ", quantity=" + quantity
                + ", transferNotes=" + transferNotes + ", createdAt=" + createdAt + "]";
    }
    
    /**
     * Generates a hash code for the InventoryTransfer entity, based on its fields.
     * * @return The hash code value.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((transferId == null) ? 0 : transferId.hashCode());
        result = prime * result + ((product == null) ? 0 : product.hashCode());
        result = prime * result + ((sourceWarehouse == null) ? 0 : sourceWarehouse.hashCode());
        result = prime * result + ((destinationWarehouse == null) ? 0 : destinationWarehouse.hashCode());
        result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
        result = prime * result + ((transferNotes == null) ? 0 : transferNotes.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        return result;
    }
    
    /**
     * Compares this InventoryTransfer entity to the specified object. The result is true if and only if 
     * the argument is not null and is an InventoryTransfer object that has the same field values.
     * * @param obj The object to compare with.
     * @return {@code true} if the objects are the same; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        InventoryTransfer other = (InventoryTransfer) obj;
        if (transferId == null) {
            if (other.transferId != null)
                return false;
        } else if (!transferId.equals(other.transferId))
            return false;
        if (product == null) {
            if (other.product != null)
                return false;
        } else if (!product.equals(other.product))
            return false;
        if (sourceWarehouse == null) {
            if (other.sourceWarehouse != null)
                return false;
        } else if (!sourceWarehouse.equals(other.sourceWarehouse))
            return false;
        if (destinationWarehouse == null) {
            if (other.destinationWarehouse != null)
                return false;
        } else if (!destinationWarehouse.equals(other.destinationWarehouse))
            return false;
        if (quantity == null) {
            if (other.quantity != null)
                return false;
        } else if (!quantity.equals(other.quantity))
            return false;
        if (transferNotes == null) {
            if (other.transferNotes != null)
                return false;
        } else if (!transferNotes.equals(other.transferNotes))
            return false;
        if (createdAt == null) {
            if (other.createdAt != null)
                return false;
        } else if (!createdAt.equals(other.createdAt))
            return false;
        return true;
    }
    
    // Getters and setters
    
    /**
     * Retrieves the unique ID of the transfer record.
     * * @return The transfer ID.
     */
    public Long getTransferId() { return transferId; }
    
    /**
     * Sets the unique ID of the transfer record.
     * * @param transferId The new transfer ID.
     */
    public void setTransferId(Long transferId) { this.transferId = transferId; }
    
    /**
     * Retrieves the product associated with the transfer.
     * * @return The product entity.
     */
    public Product getProduct() { return product; }
    
    /**
     * Sets the product associated with the transfer.
     * * @param product The product entity.
     */
    public void setProduct(Product product) { this.product = product; }
    
    /**
     * Retrieves the source warehouse.
     * * @return The source warehouse entity.
     */
    public Warehouse getSourceWarehouse() { return sourceWarehouse; }
    
    /**
     * Sets the source warehouse.
     * * @param sourceWarehouse The source warehouse entity.
     */
    public void setSourceWarehouse(Warehouse sourceWarehouse) { this.sourceWarehouse = sourceWarehouse; }
    
    /**
     * Retrieves the destination warehouse.
     * * @return The destination warehouse entity.
     */
    public Warehouse getDestinationWarehouse() { return destinationWarehouse; }
    
    /**
     * Sets the destination warehouse.
     * * @param destinationWarehouse The destination warehouse entity.
     */
    public void setDestinationWarehouse(Warehouse destinationWarehouse) { this.destinationWarehouse = destinationWarehouse; }
    
    /**
     * Retrieves the quantity transferred.
     * * @return The quantity.
     */
    public Integer getQuantity() { return quantity; }
    
    /**
     * Sets the quantity transferred.
     * * @param quantity The new quantity.
     */
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    /**
     * Retrieves the transfer notes.
     * * @return The transfer notes string.
     */
    public String getTransferNotes() { return transferNotes; }
    
    /**
     * Sets the transfer notes.
     * * @param transferNotes The new transfer notes string.
     */
    public void setTransferNotes(String transferNotes) { this.transferNotes = transferNotes; }
    
    /**
     * Retrieves the creation timestamp of the record.
     * * @return The creation timestamp.
     */
    public LocalDateTime getCreatedAt() { return createdAt; }

    /**
     * Sets the {@code createdAt} timestamp to the current time before the entity is persisted (inserted).
     */
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
       
    }
    
    /**
     * Sets the creation timestamp of the record. Used for constructor initialization or testing.
     * * @param createdAt The creation timestamp.
     */
      public void setCreatedAt(LocalDateTime createdAt) {
          this.createdAt = createdAt;
      }

}