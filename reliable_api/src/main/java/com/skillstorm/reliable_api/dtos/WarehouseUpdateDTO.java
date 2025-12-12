package com.skillstorm.reliable_api.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) used for fully updating an existing warehouse (PUT request).
 * This class enforces strict validation: all required fields (name, location, maxCapacity) 
 * must be present and non-empty, as it represents a complete replacement of the warehouse resource data.
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
public class WarehouseUpdateDTO {
    
    /**
     * The new, updated name of the warehouse. Must not be blank.
     */
    @NotBlank
    private String name;
    
    /**
     * The new, updated location or address of the warehouse. Must not be blank.
     */
    @NotBlank
    private String location;
    
    /**
     * The new, updated maximum total capacity (units/volume) the warehouse can hold.
     * Must not be null and must be at least 1.
     */
    @NotNull
    @Min(value = 1, message = "Maximum capacity must be at least 1")
    private Integer maxCapacity;
    
    // Getters and setters
    
    /**
     * Provides accessor and mutator methods for all mandatory fields:
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