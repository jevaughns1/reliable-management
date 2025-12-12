package com.skillstorm.reliable_api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.reliable_api.dtos.InventoryTransferDTO;
import com.skillstorm.reliable_api.dtos.WarehouseInventoryByWarehouseDTO;
import com.skillstorm.reliable_api.dtos.WarehouseInventoryCreateDTO;
import com.skillstorm.reliable_api.dtos.WarehouseInventoryDTO;
import com.skillstorm.reliable_api.services.WarehouseInventoryService;

import jakarta.validation.Valid;


/**
 * REST controller for managing {@code WarehouseInventory} and inventory-related actions
 * such as stocking, transfers, and expiration alerts.
 * * @author Jevaughn Stewart
 * @version 1.0
 */
@RestController
@CrossOrigin(origins = {
    "http://localhost:5173",
    "https://main.d2ndcnwudegqtw.amplifyapp.com"
})

@RequestMapping("/warehouses/inventory")
public class WarehouseInventoryController {

    private final WarehouseInventoryService inventoryService;

    /**
     * Constructs the WarehouseInventoryController, injecting the required service.
     * * @param inventoryService The service layer component handling inventory business logic.
     */
    public WarehouseInventoryController(WarehouseInventoryService inventoryService) {
        this.inventoryService = inventoryService;
     
    }

    /**
     * Handles the HTTP POST request to add a new product (initial stock) to a specific warehouse.
     * * @param warehouseId The ID of the warehouse to stock.
     * @param dto The {@code WarehouseInventoryCreateDTO} containing product ID, quantity, and location.
     * @return A {@code ResponseEntity} containing the saved {@code WarehouseInventoryDTO} and 
     * an HTTP status of CREATED (201).
     */
    @PostMapping("{warehouseId}")
    public ResponseEntity<WarehouseInventoryDTO> addProductToWarehouse(
            @PathVariable Long warehouseId,
            @RequestBody @Valid WarehouseInventoryCreateDTO dto) {

        WarehouseInventoryDTO savedInventory = inventoryService.addProductToWarehouse(warehouseId, dto);
        return  new ResponseEntity<>(savedInventory, HttpStatus.CREATED);
    }
    
    /**
     * Handles the HTTP GET request to retrieve all inventory details for a specific warehouse.
     * * @param warehouseId The ID of the warehouse whose inventory is being requested.
     * @return A {@code ResponseEntity} containing the {@code WarehouseInventoryByWarehouseDTO} 
     * which aggregates warehouse details and its inventory list, with an HTTP status of OK (200).
     */
    @GetMapping("/{warehouseId}")
    public ResponseEntity<WarehouseInventoryByWarehouseDTO> getInventoryByWarehouse(@PathVariable Long warehouseId) {
      return new ResponseEntity<>(inventoryService.getInventoryByWarehouse(warehouseId),HttpStatus.OK);
    }
    
    /**
     * Handles the HTTP GET request to retrieve all inventory items across all warehouses.
     * * @return A {@code ResponseEntity} containing a list of all {@code WarehouseInventoryDTO} objects 
     * across the entire system, with an HTTP status of OK (200).
     */
    @GetMapping()
    public ResponseEntity<List<WarehouseInventoryDTO>> getAllInventory() {
      return new ResponseEntity<>(inventoryService.getAllWarehousesInventory(),HttpStatus.OK);
    }

/**
 * Handles the HTTP POST request to transfer inventory of a single product between two warehouses.
 * This operation removes the stock from the source and creates a new entry in the destination.
 * * @param dto The {@code InventoryTransferDTO} specifying source, destination, product, and notes.
 * @return A {@code ResponseEntity} with an HTTP status of OK (200) upon successful transfer.
 */
@PostMapping("/transfer")
public ResponseEntity<Void> transferInventory(@RequestBody @Valid InventoryTransferDTO dto) {
    inventoryService.transferInventory(dto);
    return ResponseEntity.ok().build();
}

/**
 * Handles the HTTP DELETE request to remove a specific product's inventory record from a warehouse.
 * * @param warehouseId The ID of the warehouse from which to delete the inventory.
 * @param productPublicId The public ID of the product whose inventory is being deleted.
 * @return A {@code ResponseEntity} with an HTTP status of 204 No Content upon successful deletion.
 */
@DeleteMapping("/{warehouseId}/{productPublicId}")
public ResponseEntity<Void> deleteInventory(
        @PathVariable Long warehouseId,
        @PathVariable String productPublicId) {

    inventoryService.deleteInventoryFromWarehouse(warehouseId, productPublicId);
    return ResponseEntity.noContent().build();
}

/**
 * Handles the HTTP GET request to retrieve inventory items nearing their expiration date.
 * * @param days The number of days remaining until expiration to consider for the alert (e.g., 30 days).
 * @return A {@code ResponseEntity} containing a list of {@code WarehouseInventoryDTO} objects 
 * that are within the expiration threshold, with an HTTP status of OK (200).
 */
@GetMapping("/alerts/expiring/{days}")
    public ResponseEntity<List<WarehouseInventoryDTO>> getNearingExpirationAlerts(@PathVariable int days) {
        if (days < 1) {
             return ResponseEntity.badRequest().build();
        }
        List<WarehouseInventoryDTO> alerts = inventoryService.getNearingExpirationAlerts(days);
        return ResponseEntity.ok(alerts);
    }

    /**
     * Handles the HTTP GET request to retrieve inventory items that have already expired.
     * * @return A {@code ResponseEntity} containing a list of {@code WarehouseInventoryDTO} objects 
     * that are past their expiration date, with an HTTP status of OK (200).
     */
    @GetMapping("/alerts/expired")
    public ResponseEntity<List<WarehouseInventoryDTO>> getExpiredInventory() {
        List<WarehouseInventoryDTO> expiredItems = inventoryService.getExpiredInventory();
        return ResponseEntity.ok(expiredItems);
    }

}