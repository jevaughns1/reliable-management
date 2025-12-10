package com.skillstorm.reliable_api.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class WarehouseUpdateDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String location;
    @NotNull
    @Min(value = 1, message = "Maximum capacity must be at least 1")
    private Integer maxCapacity;
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Integer getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(Integer maxCapacity) { this.maxCapacity = maxCapacity; }
}
