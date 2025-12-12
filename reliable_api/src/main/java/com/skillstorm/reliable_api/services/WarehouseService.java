package com.skillstorm.reliable_api.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.skillstorm.reliable_api.dtos.WarehouseDTO;
import com.skillstorm.reliable_api.dtos.WarehousePatchDTO;
import com.skillstorm.reliable_api.dtos.WarehouseUpdateDTO;
import com.skillstorm.reliable_api.models.Warehouse;
import com.skillstorm.reliable_api.repositories.WarehouseRepo;

import jakarta.transaction.Transactional;

/**
 * Service class responsible for handling business logic related to {@code Warehouse} entities.
 * This includes CRUD operations and managing mapping between DTOs and the persistent entity model.
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
@Service
@Transactional
public class WarehouseService {

    private final WarehouseRepo warehouseRepo;
    private final ModelMapper modelMapper;

    /**
     * Constructs the WarehouseService with required dependencies and configures ModelMapper's default behavior.
     * * @param repo The repository for accessing warehouse data.
     * @param mapper The utility for converting between DTOs and entities.
     */
    public WarehouseService(WarehouseRepo repo, ModelMapper mapper) {
        this.warehouseRepo = repo;
        this.modelMapper = mapper;
        // Default ModelMapper setting for general use (e.g., PATCH operations)
        this.modelMapper.getConfiguration().setSkipNullEnabled(true); 
    }

    /**
     * Creates a new warehouse entity based on the provided DTO.
     * * @param dto The {@code WarehouseDTO} containing the data for the new warehouse.
     * @return The DTO of the newly created warehouse.
     */
    public WarehouseDTO createWarehouse(WarehouseDTO dto) {
        Warehouse entity = modelMapper.map(dto, Warehouse.class);
        Warehouse saved = warehouseRepo.save(entity);
        return toDTO(saved);
    }

    /**
     * Retrieves all warehouse records from the database.
     * Converts the list of {@code Warehouse} entities to a list of {@code WarehouseDTO}s.
     * * @return A list of all warehouses as DTOs.
     */
    public List<WarehouseDTO> getWarehouses() {
        return warehouseRepo.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Performs a full replacement update (PUT) on an existing warehouse.
     * All fields from the {@code WarehouseUpdateDTO} are mapped onto the existing entity, 
     * potentially overwriting existing non-null fields with nulls if the DTO fields are null.
     * * @param id The ID of the warehouse to update.
     * @param dto The {@code WarehouseUpdateDTO} containing the complete new data.
     * @return The DTO of the updated warehouse.
     * @throws RuntimeException if the warehouse with the given ID is not found.
     */
    public WarehouseDTO updateWarehouse(Long id, WarehouseUpdateDTO dto) {
        Warehouse existing = warehouseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));
        
        // Configure ModelMapper for PUT semantics: do NOT skip nulls
        modelMapper.getConfiguration().setSkipNullEnabled(false); 
        modelMapper.map(dto, existing);
        
        // Restore default configuration
        modelMapper.getConfiguration().setSkipNullEnabled(true); 
        
        warehouseRepo.save(existing);
        return toDTO(existing);
    }

    /**
     * Performs a partial update (PATCH) on an existing warehouse.
     * Only non-null fields from the {@code WarehousePatchDTO} are mapped onto the existing entity.
     * * @param id The ID of the warehouse to update.
     * @param dto The {@code WarehousePatchDTO} containing the fields to update.
     * @return The DTO of the partially updated warehouse.
     * @throws RuntimeException if the warehouse with the given ID is not found.
     */
    public WarehouseDTO patchWarehouse(Long id, WarehousePatchDTO dto) {
        Warehouse existing = warehouseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));
        
        // ModelMapper is configured for PATCH semantics: skip nulls is the default
        modelMapper.getConfiguration().setSkipNullEnabled(true); 
        modelMapper.map(dto, existing);
        
        warehouseRepo.save(existing);
        return toDTO(existing);
    }

    /**
     * Private helper method to convert a {@code Warehouse} entity to a {@code WarehouseDTO}.
     * * @param warehouse The source entity.
     * @return The mapped DTO.
     */
    private WarehouseDTO toDTO(Warehouse warehouse) {
        return modelMapper.map(warehouse, WarehouseDTO.class);
    }
    
    /**
     * Deletes a warehouse by its ID.
     * * @param id The ID of the warehouse to delete.
     * @throws RuntimeException if the warehouse with the given ID does not exist.
     */
    public void deleteWarehouse(Long id) {
        if (!warehouseRepo.existsById(id)) {
             throw new RuntimeException("Warehouse not found");
        }
        warehouseRepo.deleteById(id);
    }
}