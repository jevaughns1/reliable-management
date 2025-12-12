package com.skillstorm.reliable_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillstorm.reliable_api.models.Warehouse;

/**
 * Repository interface for managing {@code Warehouse} entities.
 * Extends {@code JpaRepository} to provide standard CRUD operations (Create, Read, Update, Delete)
 * for managing warehouse master data.
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
@Repository
public interface WarehouseRepo extends JpaRepository<Warehouse, Long> {

    
    
}