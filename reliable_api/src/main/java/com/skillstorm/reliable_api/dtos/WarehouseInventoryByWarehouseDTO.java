package com.skillstorm.reliable_api.dtos;

import java.util.List;

public class WarehouseInventoryByWarehouseDTO {

    private String warehouseName;
    private String warehouseLocation;
    private List<WarehouseInventoryDTO> inventory;

    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }

    public String getWarehouseLocation() { return warehouseLocation; }
    public void setWarehouseLocation(String warehouseLocation) { this.warehouseLocation = warehouseLocation; }

    public List<WarehouseInventoryDTO> getInventory() { return inventory; }
    public void setInventory(List<WarehouseInventoryDTO> inventory) { this.inventory = inventory; }
}
