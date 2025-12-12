package com.skillstorm.reliable_api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) used for initiating the transfer of inventory
 * (a specific product and quantity) between two warehouses.
 * This DTO is critical for the {@code /warehouses/inventory/transfer} endpoint.
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
public class InventoryTransferDTO {

    /**
     * The unique public ID of the product being transferred. Must not be blank.
     */
    @NotBlank
    private String productPublicId;

    /**
     * The ID of the warehouse the inventory is being moved *from*. Must not be null.
     */
    @NotNull
    private Long sourceWarehouseId;

    /**
     * The ID of the warehouse the inventory is being moved *to*. Must not be null.
     */
    @NotNull
    private Long destinationWarehouseId;
   

    /**
     * Optional notes or justification for the inventory transfer.
     */
    private String transferNotes;

    // Getters and setters

    /**
     * Provides accessor and mutator methods for all fields (productPublicId, sourceWarehouseId, 
     * destinationWarehouseId, transferNotes).
     * <p>
     * {@code getProductPublicId()}, {@code setProductPublicId(String productPublicId)}
     * <p>
     * {@code getSourceWarehouseId()}, {@code setSourceWarehouseId(Long sourceWarehouseId)}
     * <p>
     * {@code getDestinationWarehouseId()}, {@code setDestinationWarehouseId(Long destinationWarehouseId)}
     * <p>
     * {@code getTransferNotes()}, {@code setTransferNotes(String transferNotes)}
     * </p>
     */
    public String getProductPublicId() { return productPublicId; }
    public void setProductPublicId(String productPublicId) { this.productPublicId = productPublicId; }
    public Long getSourceWarehouseId() { return sourceWarehouseId; }
    public void setSourceWarehouseId(Long sourceWarehouseId) { this.sourceWarehouseId = sourceWarehouseId; }
    public Long getDestinationWarehouseId() { return destinationWarehouseId; }
    public void setDestinationWarehouseId(Long destinationWarehouseId) { this.destinationWarehouseId = destinationWarehouseId; }
  
    public String getTransferNotes() { return transferNotes; }
    public void setTransferNotes(String transferNotes) { this.transferNotes = transferNotes; }
}