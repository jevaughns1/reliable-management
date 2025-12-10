package com.skillstorm.reliable_api.dtos;

import java.util.List;

public class WarehouseDTO {

    private Long warehouseId;
    private String name;
    private String location;
    private int maxCapacity;
    private int currentCapacity;

    private List<WarehouseInventoryDTO> inventory; // List of products in this warehouse

    // Constructors
    public WarehouseDTO() {}

    public WarehouseDTO(Long warehouseId, String name, String location, int maxCapacity, int currentCapacity, List<WarehouseInventoryDTO> inventory) {
        this.warehouseId = warehouseId;
        this.name = name;
        this.location = location;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = currentCapacity;
        this.inventory = inventory;
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

    public List<WarehouseInventoryDTO> getInventory() { return inventory; }
    public void setInventory(List<WarehouseInventoryDTO> inventory) { this.inventory = inventory; }

}
