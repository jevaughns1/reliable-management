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

@Entity
@Table(name = "inventory_transfers")
public class InventoryTransfer {

    public InventoryTransfer() {
    }
    public InventoryTransfer(Product product, Warehouse sourceWarehouse, Warehouse destinationWarehouse,
            Integer quantity) {
        this.product = product;
        this.sourceWarehouse = sourceWarehouse;
        this.destinationWarehouse = destinationWarehouse;
        this.quantity = quantity;
    }
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transferId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_warehouse_id")
    private Warehouse sourceWarehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_warehouse_id")
    private Warehouse destinationWarehouse;

    @Column(nullable = false)
    private Integer quantity;

    private String transferNotes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Override
    public String toString() {
        return "InventoryTransfer [transferId=" + transferId + ", product=" + product + ", sourceWarehouse="
                + sourceWarehouse + ", destinationWarehouse=" + destinationWarehouse + ", quantity=" + quantity
                + ", transferNotes=" + transferNotes + ", createdAt=" + createdAt + "]";
    }
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
    public Long getTransferId() { return transferId; }
    public void setTransferId(Long transferId) { this.transferId = transferId; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Warehouse getSourceWarehouse() { return sourceWarehouse; }
    public void setSourceWarehouse(Warehouse sourceWarehouse) { this.sourceWarehouse = sourceWarehouse; }
    public Warehouse getDestinationWarehouse() { return destinationWarehouse; }
    public void setDestinationWarehouse(Warehouse destinationWarehouse) { this.destinationWarehouse = destinationWarehouse; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getTransferNotes() { return transferNotes; }
    public void setTransferNotes(String transferNotes) { this.transferNotes = transferNotes; }
    public LocalDateTime getCreatedAt() { return createdAt; }

      @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
       
    }
      public void setCreatedAt(LocalDateTime createdAt) {
          this.createdAt = createdAt;
      }

}
