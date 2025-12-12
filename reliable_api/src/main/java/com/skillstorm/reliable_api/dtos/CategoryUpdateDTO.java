package com.skillstorm.reliable_api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) used for fully updating an existing product category (PUT request).
 * This class enforces strict validation: all fields required for a category update must be
 * present and non-empty, as it represents a complete replacement of the resource.
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
public class CategoryUpdateDTO {

    /**
     * The new, updated name of the category. Must not be blank and has a maximum size of 150 characters.
     */
    @Size(max = 150)
    @NotBlank
    private String name;

    /**
     * The new, updated description of the category. Must be present (not null).
     */
    @NotNull 
    private String description;

    // Getters and Setters

    /**
     * Provides accessor and mutator methods for the mandatory fields (name, description).
     * <p>
     * {@code getName()}, {@code setName(String name)}
     * <p>
     * {@code getDescription()}, {@code setDescription(String description)}
     * </p>
     */
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    

}