package com.skillstorm.reliable_api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) for representing product category information.
 * This class is used for data exchange, particularly during the creation and retrieval 
 * of category entities. It includes validation constraints for the name field.
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
public class CategoryDTO {

    /**
     * The name of the category. Must not be blank and has a maximum size of 150 characters.
     */
    @NotBlank
    @Size(max = 150)
    private String name;

    /**
     * A detailed description of the category (optional).
     */
    private String description;

    /**
     * The unique identifier (ID) of the category. This is typically set by the database
     * and used for retrieval/updates.
     */
    private Long id;

    // Getters and Setters

    /**
     * Provides accessor and mutator methods for all fields (id, name, description).
     * <p>
     * {@code getId()}, {@code setId(Long id)}
     * <p>
     * {@code getName()}, {@code setName(String name)}
     * <p>
     * {@code getDescription()}, {@code setDescription(String description)}
     * </p>
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    } 

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