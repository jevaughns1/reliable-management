package com.skillstorm.reliable_api.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.skillstorm.reliable_api.dtos.ProductDTO;
import com.skillstorm.reliable_api.dtos.WarehouseInventoryByWarehouseDTO;
import com.skillstorm.reliable_api.dtos.WarehouseInventoryCreateDTO;
import com.skillstorm.reliable_api.dtos.WarehouseInventoryDTO;
import com.skillstorm.reliable_api.exceptions.ResourceNotFoundException;
import com.skillstorm.reliable_api.models.Product;
import com.skillstorm.reliable_api.models.Warehouse;
import com.skillstorm.reliable_api.models.WarehouseInventory;
import com.skillstorm.reliable_api.repositories.ProductRepo;
import com.skillstorm.reliable_api.repositories.WarehouseInventoryRepo;
import com.skillstorm.reliable_api.repositories.WarehouseRepo;

import jakarta.transaction.Transactional;

@Service
public class WarehouseInventoryService {

    private final WarehouseRepo warehouseRepo;
    private final ProductRepo productRepo;
    private final WarehouseInventoryRepo warehouseInventoryRepo;
    private final ModelMapper modelMapper;

    public WarehouseInventoryService(
            WarehouseInventoryRepo warehouseInventoryRepo,
            WarehouseRepo warehouseRepo,
            ProductRepo productRepo,
            ModelMapper modelMapper) {
        this.warehouseInventoryRepo = warehouseInventoryRepo;
        this.warehouseRepo = warehouseRepo;
        this.productRepo = productRepo;
        this.modelMapper = modelMapper;
    }


    @Transactional
    public WarehouseInventoryDTO addProductToWarehouse(Long warehouseId, WarehouseInventoryCreateDTO dto) {

        
        Warehouse warehouse = warehouseRepo.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with ID: " + warehouseId));

  
        Product product = productRepo.findByPublicIdAndIsDeletedFalse(dto.getProductPublicId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with publicId: " + dto.getProductPublicId()));

     
        if (warehouseInventoryRepo.findByProduct_ProductId(product.getProductId()).isPresent()) {
            throw new IllegalStateException("Product is already assigned to a warehouse");
        }

        // Check warehouse capacity
        int currentCapacity = warehouse.getCurrentCapacity();
        if (currentCapacity + dto.getQuantity() > warehouse.getMaxCapacity()) {
            throw new IllegalStateException("Adding this quantity exceeds warehouse capacity");
        }

      
        warehouse.setCurrentCapacity(currentCapacity + dto.getQuantity());

        // Create inventory record
        WarehouseInventory inventory = new WarehouseInventory();
        inventory.setWarehouse(warehouse);
        inventory.setProduct(product);
        inventory.setQuantity(dto.getQuantity());
        inventory.setStorageLocation(dto.getStorageLocation());
        inventory.setExpirationDate(dto.getExpirationDate());

        WarehouseInventory savedInventory = warehouseInventoryRepo.save(inventory);

        // Map to DTO manually because nested fields need custom mapping
        WarehouseInventoryDTO response = new WarehouseInventoryDTO();
        response.setProductPublicId(savedInventory.getProduct().getPublicId());
        response.setQuantity(savedInventory.getQuantity());
        response.setWarehouseName(savedInventory.getWarehouse().getName());
        response.setWarehouseLocation(savedInventory.getWarehouse().getLocation());
        response.setProduct(modelMapper.map(savedInventory.getProduct(), ProductDTO.class));
        response.setExpirationDate(savedInventory.getExpirationDate());
        response.setStorageLocation(savedInventory.getStorageLocation());

        return response;
    }

    /**
     * Get inventory for a single warehouse with all product details
     */
    @Transactional
    public WarehouseInventoryByWarehouseDTO getInventoryByWarehouse(Long warehouseId) {
        Warehouse warehouse = warehouseRepo.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));

        List<WarehouseInventory> inventories = warehouseInventoryRepo.findAllWithProductsByWarehouseId(warehouseId);

        List<WarehouseInventoryDTO> inventoryDTOs = inventories.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        // Map warehouse info + inventory list into a single DTO
        WarehouseInventoryByWarehouseDTO result = new WarehouseInventoryByWarehouseDTO();
        result.setWarehouseName(warehouse.getName());
        result.setWarehouseLocation(warehouse.getLocation());
        result.setInventory(inventoryDTOs);

        return result;
    }

    /**
     * Get inventory for all warehouses with product details
     */
    public List<WarehouseInventoryDTO> getAllWarehousesInventory() {
        List<WarehouseInventory> inventories = warehouseInventoryRepo.findAllWithWarehouseAndProduct();

        return inventories.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Maps WarehouseInventory entity to DTO
     * Nested product and warehouse fields are mapped manually if needed
     */
    private WarehouseInventoryDTO toDTO(WarehouseInventory inventory) {
        WarehouseInventoryDTO dto = new WarehouseInventoryDTO();
        dto.setProductPublicId(inventory.getProduct().getPublicId());
        dto.setQuantity(inventory.getQuantity());
        dto.setWarehouseName(inventory.getWarehouse().getName());
        dto.setWarehouseLocation(inventory.getWarehouse().getLocation());
        dto.setProduct(modelMapper.map(inventory.getProduct(), ProductDTO.class));
        dto.setExpirationDate(inventory.getExpirationDate());
        dto.setStorageLocation(inventory.getStorageLocation());
        return dto;
    }
}
