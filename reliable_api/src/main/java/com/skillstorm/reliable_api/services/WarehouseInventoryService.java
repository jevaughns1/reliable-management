package com.skillstorm.reliable_api.services;

import java.time.LocalDate;
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

/**
 * Service class responsible for managing inventory levels, transfers, and capacity 
 * checks within warehouses.
 * <p>
 * Implements the core business rule: A product can only be stocked in one warehouse at a time.
 * </p>
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
@Service
@Transactional
public class WarehouseInventoryService {

    private final WarehouseRepo warehouseRepo;
    private final ProductRepo productRepo;
    private final WarehouseInventoryRepo warehouseInventoryRepo;
    private final InventoryTransferRepo inventoryTransferRepo;
    private final ModelMapper modelMapper;

    /**
     * Constructs the WarehouseInventoryService with all necessary repository and mapper dependencies.
     */
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

    /**
     * Adds a new product inventory record to a specified warehouse.
     * Enforces the business rule that a product can only be assigned to one warehouse globally.
     * Updates the warehouse's {@code currentCapacity}.
     *
     * @param warehouseId The ID of the target warehouse.
     * @param dto The creation DTO containing product public ID and initial quantity/details.
     * @return The created inventory record as a DTO.
     * @throws ResourceNotFoundException if the warehouse or product is not found.
     * @throws IllegalStateException if the product is already in another warehouse or capacity is exceeded.
     */
    public WarehouseInventoryDTO addProductToWarehouse(Long warehouseId, WarehouseInventoryCreateDTO dto) {

        Warehouse warehouse = fetchWarehouse(warehouseId);
        Product product = fetchProduct(dto.getProductPublicId());

        // GLOBAL BUSINESS RULE: A product can only exist in ONE warehouse total.
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
    
    /**
     * Deletes a product's entire inventory record from a specific warehouse.
     * This method is used when the product is entirely removed from the warehouse (quantity goes to zero).
     * Updates the warehouse's {@code currentCapacity}.
     *
     * @param warehouseId The ID of the warehouse.
     * @param productPublicId The public ID of the product to remove.
     * @throws ResourceNotFoundException if the warehouse, product, or inventory record is not found.
     * @throws IllegalStateException if the capacity calculation results in a negative value (data inconsistency).
     */
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

    /**
     * Retrieves all inventory records for a specific warehouse, including product details.
     * Filters out products that have been soft-deleted.
     *
     * @param warehouseId The ID of the warehouse.
     * @return A DTO containing warehouse details and a list of its inventory items.
     * @throws ResourceNotFoundException if the warehouse is not found.
     */
    @Transactional(Transactional.TxType.SUPPORTS)
    public WarehouseInventoryByWarehouseDTO getInventoryByWarehouse(Long warehouseId) {

        Warehouse warehouse = fetchWarehouse(warehouseId);

        List<WarehouseInventory> inventories =
                warehouseInventoryRepo.findAllWithProductsByWarehouseIdAndIsDeletedFalse(warehouseId);

        List<WarehouseInventoryDTO> inventoryDTOs = inventories.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        WarehouseInventoryByWarehouseDTO result = new WarehouseInventoryByWarehouseDTO();
        result.setWarehouseName(warehouse.getName());
        result.setWarehouseLocation(warehouse.getLocation());
        result.setInventory(inventoryDTOs);

        return result;
    }

    /**
     * Retrieves all inventory records across all warehouses, with eager fetching of warehouse and product data.
     *
     * @return A flat list of all inventory records as DTOs.
     */
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<WarehouseInventoryDTO> getAllWarehousesInventory() {

        List<WarehouseInventory> inventories =
                warehouseInventoryRepo.findAllWithWarehouseAndProduct();

        return inventories.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Performs a full inventory transfer for a product from a source to a destination warehouse.
     * The entire quantity of the product is moved, and a new {@code InventoryTransfer} log is created.
     *
     * @param dto The transfer request DTO.
     * @throws IllegalArgumentException if source and destination are the same.
     * @throws ResourceNotFoundException if any warehouse or product is not found.
     * @throws IllegalStateException if the product is not in the source warehouse, quantity is zero, or destination capacity is exceeded.
     */
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

    /**
     * Helper method to retrieve a {@code Warehouse} entity by ID, throwing a {@code ResourceNotFoundException} if not found.
     * * @param id The ID of the warehouse.
     * @return The found {@code Warehouse} entity.
     * @throws ResourceNotFoundException if the warehouse is not found.
     */
    private Warehouse fetchWarehouse(Long id) {
        return warehouseRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Warehouse not found with ID: " + id));
    }

    /**
     * Helper method to retrieve an active {@code Product} entity by its public ID, throwing a {@code ResourceNotFoundException} if not found or deleted.
     * * @param publicId The public ID of the product.
     * @return The found active {@code Product} entity.
     * @throws ResourceNotFoundException if the product is not found or is soft-deleted.
     */
    private Product fetchProduct(String publicId) {
        return productRepo.findByPublicIdAndIsDeletedFalse(publicId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with publicId: " + publicId));
    }

    /**
     * Helper method to check if adding a specified quantity would exceed the warehouse's maximum capacity.
     * * @param warehouse The warehouse entity.
     * @param additionalQuantity The quantity to be added.
     * @throws IllegalStateException if capacity would be exceeded.
     */
    private void checkWarehouseCapacity(Warehouse warehouse, int additionalQuantity) {
        int newCapacity = warehouse.getCurrentCapacity() + additionalQuantity;
        if (newCapacity > warehouse.getMaxCapacity()) {
            throw new IllegalStateException(
                    "Warehouse capacity exceeded. Max: " + warehouse.getMaxCapacity() +
                    ", Requested New Capacity: " + newCapacity);
        }
    }

    /**
     * Retrieves inventory items that are nearing their expiration date.
     * The expiration range is from today up to, but not including, {@code today + days}.
     *
     * @param days The number of days out to check for expiration.
     * @return A list of {@code WarehouseInventoryDTO}s for items nearing expiration.
     */
    public List<WarehouseInventoryDTO> getNearingExpirationAlerts(int days) {
        LocalDate today = LocalDate.now();
        LocalDate expirationThreshold = today.plusDays(days);
        
        List<WarehouseInventory> expiringItems = 
            warehouseInventoryRepo.findByExpirationDateBetween(today, expirationThreshold);

        // Convert entities to DTOs for safe transfer to the controller/frontend
        return expiringItems.stream()
            .map(item -> modelMapper.map(item, WarehouseInventoryDTO.class))
            .collect(Collectors.toList());
    }
    
    /**
     * Retrieves inventory items that have already expired (expiration date is before today).
     * * @return A list of {@code WarehouseInventoryDTO}s for expired items.
     */
    public List<WarehouseInventoryDTO> getExpiredInventory() {
        List<WarehouseInventory> expiredItems = 
            warehouseInventoryRepo.findByExpirationDateBefore(LocalDate.now());

        return expiredItems.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Private helper method to map a {@code WarehouseInventory} entity to a {@code WarehouseInventoryDTO}.
     * Includes mapping the nested {@code Product} entity to a {@code ProductDTO}.
     * * @param inventory The source {@code WarehouseInventory} entity.
     * @return The mapped {@code WarehouseInventoryDTO}.
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