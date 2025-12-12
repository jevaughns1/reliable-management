package com.skillstorm.reliable_api.dtos;

import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) used for partially updating an existing product category (PATCH request).
 * This class contains optional fields, allowing the user to update only the specific 
 * properties they wish to change without sending the entire object.
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
public class CategoryPatchDTO {
    
    /**
     * The updated name of the category. This field is optional for a patch operation.
     * Has a maximum size of 150 characters.
     */
    @Size(max = 150)
    private String name;

    /**
     * The updated detailed description of the category. This field is optional for a patch operation.
     * Has a maximum size of 300 characters.
     */
    @Size(max = 300)
    private String description;

    // Getters and Setters

    /**
     * Provides accessor and mutator methods for the fields (name, description).
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