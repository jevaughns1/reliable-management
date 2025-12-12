package com.skillstorm.reliable_api.dtos;

import java.util.List;

/**
 * Data Transfer Object (DTO) for representing detailed warehouse information.
 * This class includes basic warehouse attributes, capacity metrics, and a list 
 * of the inventory currently held within the warehouse.
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
public class WarehouseDTO {

    /**
     * The unique database ID of the warehouse.
     */
    private Long warehouseId;
    
    /**
     * The name of the warehouse (e.g., "Main Distribution Center").
     */
    private String name;
    
    /**
     * The physical location or address of the warehouse.
     */
    private String location;
    
    /**
     * The maximum total capacity (units/volume) the warehouse can hold.
     */
    private int maxCapacity;
    
    /**
     * The current utilized capacity (units/volume) in the warehouse.
     */
    private int currentCapacity;

    /**
     * A list of {@code WarehouseInventoryDTO} objects, representing the various 
     * products and their stock levels currently in this warehouse.
     */
    private List<WarehouseInventoryDTO> inventory; 

    // Constructors

    /**
     * Default constructor.
     */
    public WarehouseDTO() {}

    /**
     * Full parameterized constructor for creating a complete {@code WarehouseDTO} instance.
     * * @param warehouseId The unique ID of the warehouse.
     * @param name The name of the warehouse.
     * @param location The location of the warehouse.
     * @param maxCapacity The maximum storage capacity.
     * @param currentCapacity The currently used capacity.
     * @param inventory The list of product inventory items in the warehouse.
     */
    public WarehouseDTO(Long warehouseId, String name, String location, int maxCapacity, int currentCapacity, List<WarehouseInventoryDTO> inventory) {
        this.warehouseId = warehouseId;
        this.name = name;
        this.location = location;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = currentCapacity;
        this.inventory = inventory;
    }

    // Getters & Setters
    
    /**
     * Provides accessor and mutator methods for all fields:
     * (warehouseId, name, location, maxCapacity, currentCapacity, inventory).
     * <p>
     * {@code getWarehouseId()}, {@code setWarehouseId(Long warehouseId)}
     * <p>
     * {@code getName()}, {@code setName(String name)}
     * <p>
     * {@code getLocation()}, {@code setLocation(String location)}
     * <p>
     * {@code getMaxCapacity()}, {@code setMaxCapacity(int maxCapacity)}
     * <p>
     * {@code getCurrentCapacity()}, {@code setCurrentCapacity(int currentCapacity)}
     * <p>
     * {@code getInventory()}, {@code setInventory(List<WarehouseInventoryDTO> inventory)}
     * </p>
     */
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