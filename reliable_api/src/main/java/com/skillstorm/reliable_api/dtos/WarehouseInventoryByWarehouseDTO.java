package com.skillstorm.reliable_api.dtos;

import java.util.List;

/**
 * Data Transfer Object (DTO) used for retrieving a comprehensive view of inventory
 * specific to a single warehouse.
 * This DTO aggregates the warehouse's identifying information (name, location)
 * with the detailed list of products it currently holds.
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
public class WarehouseInventoryByWarehouseDTO {

    /**
     * The name of the warehouse (e.g., "Main Distribution Center").
     */
    private String warehouseName;
    
    /**
     * The physical location or address of the warehouse.
     */
    private String warehouseLocation;
    
    /**
     * A detailed list of {@code WarehouseInventoryDTO} objects representing the specific 
     * products and their stock levels within this warehouse.
     */
    private List<WarehouseInventoryDTO> inventory;

    // Getters and Setters

    /**
     * Provides accessor and mutator methods for all fields:
     * (warehouseName, warehouseLocation, inventory).
     * <p>
     * {@code getWarehouseName()}, {@code setWarehouseName(String warehouseName)}
     * <p>
     * {@code getWarehouseLocation()}, {@code setWarehouseLocation(String warehouseLocation)}
     * <p>
     * {@code getInventory()}, {@code setInventory(List<WarehouseInventoryDTO> inventory)}
     * </p>
     */
    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }

    public String getWarehouseLocation() { return warehouseLocation; }
    public void setWarehouseLocation(String warehouseLocation) { this.warehouseLocation = warehouseLocation; }

    public List<WarehouseInventoryDTO> getInventory() { return inventory; }
    public void setInventory(List<WarehouseInventoryDTO> inventory) { this.inventory = inventory; }
}