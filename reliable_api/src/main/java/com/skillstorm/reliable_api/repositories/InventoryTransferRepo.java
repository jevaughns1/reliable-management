package com.skillstorm.reliable_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillstorm.reliable_api.models.InventoryTransfer;

/**
 * Repository interface for managing {@code InventoryTransfer} entities.
 * Extends {@code JpaRepository} to provide standard CRUD operations for 
 * recording the movement of products between warehouses.
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
@Repository
public interface InventoryTransferRepo extends JpaRepository<InventoryTransfer, Long> {
}