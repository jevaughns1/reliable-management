package com.skillstorm.reliable_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillstorm.reliable_api.models.InventoryTransfer;

@Repository
public interface InventoryTransferRepo extends JpaRepository<InventoryTransfer, Long> {
}
