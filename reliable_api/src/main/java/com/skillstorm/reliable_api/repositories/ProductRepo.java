package com.skillstorm.reliable_api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillstorm.reliable_api.models.Product;

/**
 * Repository interface for managing {@code Product} entities.
 * Extends {@code JpaRepository} to provide standard CRUD operations and custom query methods
 * for filtering based on the public ID and soft-delete status.
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    /**
     * Retrieves an {@code Optional} containing a {@code Product} entity found by its unique public ID,
     * but only if the product has not been logically deleted (i.e., {@code isDeleted} is false).
     * This method is the standard way to retrieve products via the exposed API identifier.
     * * @param publicId The unique public identifier (UUID string) of the product.
     * @return An {@code Optional} containing the found {@code Product}, or {@code Optional.empty()} if not found or deleted.
     */
    Optional<Product>  findByPublicIdAndIsDeletedFalse(String publicId);
    
    /**
     * Retrieves a list of all {@code Product} entities that have not been logically deleted
     * (i.e., where {@code isDeleted} is false).
     * * @return A list of all active {@code Product} entities.
     */
    List<Product>  findAllByIsDeletedFalse();
}