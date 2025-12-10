package com.skillstorm.reliable_api.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.skillstorm.reliable_api.dtos.InventoryTransferDTO;
import com.skillstorm.reliable_api.dtos.ProductDTO;
import com.skillstorm.reliable_api.dtos.WarehouseInventoryByWarehouseDTO;
import com.skillstorm.reliable_api.dtos.WarehouseInventoryCreateDTO;
import com.skillstorm.reliable_api.dtos.WarehouseInventoryDTO;
import com.skillstorm.reliable_api.exceptions.ResourceNotFoundException;
import com.skillstorm.reliable_api.models.InventoryTransfer;
import com.skillstorm.reliable_api.models.Product;
import com.skillstorm.reliable_api.models.Warehouse;
import com.skillstorm.reliable_api.models.WarehouseInventory;
import com.skillstorm.reliable_api.repositories.InventoryTransferRepo;
import com.skillstorm.reliable_api.repositories.ProductRepo;
import com.skillstorm.reliable_api.repositories.WarehouseInventoryRepo;
import com.skillstorm.reliable_api.repositories.WarehouseRepo;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class WarehouseInventoryService {

    private final WarehouseRepo warehouseRepo;
    private final ProductRepo productRepo;
    private final WarehouseInventoryRepo warehouseInventoryRepo;
    private final InventoryTransferRepo inventoryTransferRepo;
    private final ModelMapper modelMapper;

    public WarehouseInventoryService(
            WarehouseInventoryRepo warehouseInventoryRepo,
            WarehouseRepo warehouseRepo,
            ProductRepo productRepo,
            InventoryTransferRepo inventoryTransferRepo,
            ModelMapper modelMapper) {

        this.warehouseInventoryRepo = warehouseInventoryRepo;
        this.warehouseRepo = warehouseRepo;
        this.productRepo = productRepo;
        this.inventoryTransferRepo = inventoryTransferRepo;
        this.modelMapper = modelMapper;
    }


    public WarehouseInventoryDTO addProductToWarehouse(Long warehouseId, WarehouseInventoryCreateDTO dto) {

        Warehouse warehouse = fetchWarehouse(warehouseId);
        Product product = fetchProduct(dto.getProductPublicId());

        // GLOBAL BUSINESS RULE:
        // A product can only exist in ONE warehouse total.
        warehouseInventoryRepo.findByProduct_ProductId(product.getProductId())
                .ifPresent(existing -> {
                    throw new IllegalStateException(
                        "Product is already assigned to warehouse with ID: " +
                        existing.getWarehouse().getWarehouseId()
                    );
                });

        // Capacity check for incoming quantity
        checkWarehouseCapacity(warehouse, dto.getQuantity());

        // Update current capacity of warehouse
        warehouse.setCurrentCapacity(warehouse.getCurrentCapacity() + dto.getQuantity());
        warehouseRepo.save(warehouse);

        // Create new inventory record
        WarehouseInventory inventory = new WarehouseInventory();
        inventory.setWarehouse(warehouse);
        inventory.setProduct(product);
        inventory.setQuantity(dto.getQuantity());
        inventory.setStorageLocation(dto.getStorageLocation());
        inventory.setExpirationDate(dto.getExpirationDate());

        WarehouseInventory savedInventory = warehouseInventoryRepo.save(inventory);

        return toDTO(savedInventory);
    }
    @Transactional
public void deleteInventoryFromWarehouse(Long warehouseId, String productPublicId) {

    Warehouse warehouse = fetchWarehouse(warehouseId);
    Product product = fetchProduct(productPublicId);

    // Find inventory row for this (warehouse, product)
    WarehouseInventory inventory = warehouseInventoryRepo
            .findByWarehouseAndProduct(warehouse, product)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Product not found in this warehouse inventory"));

    int quantity = inventory.getQuantity();

    // Adjust capacity
    int newCapacity = warehouse.getCurrentCapacity() - quantity;
    if (newCapacity < 0) {
        throw new IllegalStateException(
                "Warehouse capacity would become negative; inconsistent data"
        );
    }

    warehouse.setCurrentCapacity(newCapacity);

    // Remove inventory row entirely
    warehouseInventoryRepo.delete(inventory);

    // Save updated warehouse
    warehouseRepo.save(warehouse);
}


    @Transactional(Transactional.TxType.SUPPORTS)
    public WarehouseInventoryByWarehouseDTO getInventoryByWarehouse(Long warehouseId) {

        Warehouse warehouse = fetchWarehouse(warehouseId);

        List<WarehouseInventory> inventories =
                warehouseInventoryRepo.findAllWithProductsByWarehouseId(warehouseId);

        List<WarehouseInventoryDTO> inventoryDTOs = inventories.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        WarehouseInventoryByWarehouseDTO result = new WarehouseInventoryByWarehouseDTO();
        result.setWarehouseName(warehouse.getName());
        result.setWarehouseLocation(warehouse.getLocation());
        result.setInventory(inventoryDTOs);

        return result;
    }

 
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<WarehouseInventoryDTO> getAllWarehousesInventory() {

        List<WarehouseInventory> inventories =
                warehouseInventoryRepo.findAllWithWarehouseAndProduct();

        return inventories.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

  
    public void transferInventory(InventoryTransferDTO dto) {

        Product product = fetchProduct(dto.getProductPublicId());
        Warehouse source = fetchWarehouse(dto.getSourceWarehouseId());
        Warehouse destination = fetchWarehouse(dto.getDestinationWarehouseId());

        if (source.getWarehouseId().equals(destination.getWarehouseId())) {
            throw new IllegalArgumentException("Source and destination warehouses cannot be the same");
        }

        // Inventory in source
        WarehouseInventory sourceInventory =
                warehouseInventoryRepo.findByWarehouseAndProduct(source, product)
                        .orElseThrow(() -> new IllegalStateException(
                                "Product not found in source warehouse"));

        Integer quantityToTransfer = sourceInventory.getQuantity();
        if (quantityToTransfer == null || quantityToTransfer <= 0) {
            throw new IllegalStateException("No quantity available to transfer");
        }

        // Check destination capacity
        checkWarehouseCapacity(destination, quantityToTransfer);

        // Adjust capacities
        int newSourceCapacity = source.getCurrentCapacity() - quantityToTransfer;
        if (newSourceCapacity < 0) {
            throw new IllegalStateException("Source warehouse capacity would become negative");
        }

        source.setCurrentCapacity(newSourceCapacity);
        destination.setCurrentCapacity(destination.getCurrentCapacity() + quantityToTransfer);

        // Remove inventory row from source (since product can't exist in multiple warehouses)
        warehouseInventoryRepo.delete(sourceInventory);

        // Create new inventory row for destination
        WarehouseInventory destInventory = new WarehouseInventory();
        destInventory.setWarehouse(destination);
        destInventory.setProduct(product);
        destInventory.setQuantity(quantityToTransfer);
        // carry over optional fields; or you could let DTO define dest location later
        destInventory.setExpirationDate(sourceInventory.getExpirationDate());
        destInventory.setStorageLocation(sourceInventory.getStorageLocation());

        warehouseRepo.save(source);
        warehouseRepo.save(destination);
        warehouseInventoryRepo.save(destInventory);

        // Log transfer
        InventoryTransfer transfer = new InventoryTransfer();
        transfer.setProduct(product);
        transfer.setSourceWarehouse(source);
        transfer.setDestinationWarehouse(destination);
        transfer.setQuantity(quantityToTransfer);
        transfer.setTransferNotes(dto.getTransferNotes());
        transfer.setCreatedAt(LocalDateTime.now());
        inventoryTransferRepo.save(transfer);
    }

 //Helpers
    private Warehouse fetchWarehouse(Long id) {
        return warehouseRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Warehouse not found with ID: " + id));
    }

    private Product fetchProduct(String publicId) {
        return productRepo.findByPublicIdAndIsDeletedFalse(publicId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with publicId: " + publicId));
    }

  
    private void checkWarehouseCapacity(Warehouse warehouse, int additionalQuantity) {
        int newCapacity = warehouse.getCurrentCapacity() + additionalQuantity;
        if (newCapacity > warehouse.getMaxCapacity()) {
            throw new IllegalStateException(
                    "Warehouse capacity exceeded. Max: " + warehouse.getMaxCapacity() +
                    ", Requested New Capacity: " + newCapacity);
        }
    }

    /**
     * Map WarehouseInventory -> WarehouseInventoryDTO
     * with nested ProductDTO.
     */
    private WarehouseInventoryDTO toDTO(WarehouseInventory inventory) {
        WarehouseInventoryDTO dto = new WarehouseInventoryDTO();
        dto.setProductPublicId(inventory.getProduct().getPublicId());
        dto.setQuantity(inventory.getQuantity());
        dto.setStorageLocation(inventory.getStorageLocation());
        dto.setExpirationDate(inventory.getExpirationDate());

        ProductDTO productDTO = modelMapper.map(inventory.getProduct(), ProductDTO.class);
        dto.setProduct(productDTO);

        return dto;
    }
}
