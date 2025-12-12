package com.skillstorm.reliable_api.repositories;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillstorm.reliable_api.models.Category;

/**
 * Repository interface for managing {@code Category} entities.
 * Extends {@code JpaRepository} to provide standard CRUD operations and custom query methods.
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
public interface CategoryRepo extends JpaRepository<Category, Long> {

    /**
     * Checks if a category with the given name exists, ignoring case sensitivity.
     * This is useful for enforcing uniqueness constraints on the category name
     * regardless of capitalization.
     * * * @param name The name of the category to check.
     * @return {@code true} if a category with the name exists (case-insensitive), {@code false} otherwise.
     */
    boolean existsByNameAllIgnoreCase(String name);
    
    /**
     * Retrieves all categories from the database, ordered alphabetically by name in ascending order.
     * * * @return A list of all {@code Category} entities sorted by name.
     */
    List<Category> findAllByOrderByNameAsc();
    
}