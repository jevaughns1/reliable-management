package com.skillstorm.reliable_api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class InventoryTransferDTO {

    @NotBlank
    private String productPublicId;

    @NotNull
    private Long sourceWarehouseId;

    @NotNull
    private Long destinationWarehouseId;
   

    private String transferNotes;

    // Getters and setters
    public String getProductPublicId() { return productPublicId; }
    public void setProductPublicId(String productPublicId) { this.productPublicId = productPublicId; }
    public Long getSourceWarehouseId() { return sourceWarehouseId; }
    public void setSourceWarehouseId(Long sourceWarehouseId) { this.sourceWarehouseId = sourceWarehouseId; }
    public Long getDestinationWarehouseId() { return destinationWarehouseId; }
    public void setDestinationWarehouseId(Long destinationWarehouseId) { this.destinationWarehouseId = destinationWarehouseId; }
  
    public String getTransferNotes() { return transferNotes; }
    public void setTransferNotes(String transferNotes) { this.transferNotes = transferNotes; }
}
