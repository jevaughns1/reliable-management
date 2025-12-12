package com.skillstorm.reliable_api.dtos;

import jakarta.validation.constraints.Min;

/**
 * Data Transfer Object (DTO) used for partially updating an existing warehouse (PATCH request).
 * This class contains optional fields, allowing users to update specific warehouse
 * properties like name, location, or maximum capacity without needing to send the entire object.
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
public class WarehousePatchDTO {
    
    /**
     * The updated name of the warehouse. This field is optional for a patch operation.
     */
    private String name;
    
    /**
     * The updated location or address of the warehouse. This field is optional for a patch operation.
     */
    private String location;

    /**
     * The updated maximum total capacity (units/volume) the warehouse can hold.
     * This field is optional but must be at least 1 if provided.
     */
    @Min(value = 1, message = "Maximum capacity must be at least 1")
    private Integer maxCapacity;
    
    // Getters and setters
    
    /**
     * Provides accessor and mutator methods for all fields:
     * (name, location, maxCapacity).
     * <p>
     * {@code getName()}, {@code setName(String name)}
     * <p>
     * {@code getLocation()}, {@code setLocation(String location)}
     * <p>
     * {@code getMaxCapacity()}, {@code setMaxCapacity(Integer maxCapacity)}
     * </p>
     */
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Integer getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(Integer maxCapacity) { this.maxCapacity = maxCapacity; }
}