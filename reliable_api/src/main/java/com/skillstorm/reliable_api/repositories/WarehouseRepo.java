package com.skillstorm.reliable_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillstorm.reliable_api.models.Warehouse;

@Repository
public interface WarehouseRepo extends JpaRepository<Warehouse, Long> {

    
    
}
