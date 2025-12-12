package com.skillstorm.reliable_api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.reliable_api.dtos.WarehouseDTO;
import com.skillstorm.reliable_api.dtos.WarehousePatchDTO;
import com.skillstorm.reliable_api.dtos.WarehouseUpdateDTO;
import com.skillstorm.reliable_api.services.WarehouseService;

import jakarta.validation.Valid;

/**
 * REST controller for managing {@code Warehouse} entities.
 * This class provides standard CRUD (Create, Read, Update, Delete) operations
 * for warehouse master data.
 * * @author Jevaughn Stewart
 * @version 1.0
 */
@RestController
@CrossOrigin(origins = {
    "http://localhost:5173",
    "https://main.d2ndcnwudegqtw.amplifyapp.com"
})
@RequestMapping("/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    /**
     * Constructs the WarehouseController, injecting the required WarehouseService.
     * * @param service The service layer component responsible for warehouse business logic.
     */
    public WarehouseController(WarehouseService service) {
        this.warehouseService = service;
    }

    /**
     * Handles the HTTP GET request to retrieve all warehouses.
     * * @return A {@code ResponseEntity} containing a list of all {@code WarehouseDTO} objects 
     * and an HTTP status of OK (200).
     */
    @GetMapping
    public ResponseEntity<List<WarehouseDTO>> getWarehouses() {
        return ResponseEntity.ok(warehouseService.getWarehouses());
    }

    /**
     * Handles the HTTP POST request to create a new warehouse.
     * * @param dto The {@code WarehouseDTO} containing the data for the new warehouse.
     * @return A {@code ResponseEntity} containing the newly created {@code WarehouseDTO} and an 
     * HTTP status of CREATED (201).
     */
    @PostMapping
    public ResponseEntity<WarehouseDTO> createWarehouse(@Valid @RequestBody WarehouseDTO dto) {
        return new ResponseEntity<>(warehouseService.createWarehouse(dto), HttpStatus.CREATED);
    }

    /**
     * Handles the HTTP PUT request to fully update an existing warehouse identified by its ID.
     * * @param id The ID of the warehouse to update.
     * @param dto The {@code WarehouseUpdateDTO} containing the full set of updated warehouse data.
     * @return A {@code ResponseEntity} containing the updated {@code WarehouseDTO} and an 
     * HTTP status of OK (200).
     */
    @PutMapping("/{id}")
    public ResponseEntity<WarehouseDTO> updateWarehouse(@PathVariable Long id,
                                                        @Valid @RequestBody WarehouseUpdateDTO dto) {
        return ResponseEntity.ok(warehouseService.updateWarehouse(id, dto));
    }

    /**
     * Handles the HTTP PATCH request to partially update an existing warehouse identified by its ID.
     * This allows for updating specific fields without requiring all warehouse data.
     * * @param id The ID of the warehouse to partially update.
     * @param dto The {@code WarehousePatchDTO} containing only the fields to be updated.
     * @return A {@code ResponseEntity} containing the updated {@code WarehouseDTO} and an 
     * HTTP status of OK (200).
     */
    @PatchMapping("/{id}")
    public ResponseEntity<WarehouseDTO> patchWarehouse(@PathVariable Long id,
                                                       @Valid @RequestBody WarehousePatchDTO dto) {
        return ResponseEntity.ok(warehouseService.patchWarehouse(id, dto));
    }
    
    /**
     * Handles the HTTP DELETE request to delete a warehouse by its ID.
     * * @param id The ID of the warehouse to delete.
     * @return A {@code ResponseEntity} with an HTTP status of 204 No Content upon successful deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        // Returns 204 No Content on successful deletion
        return ResponseEntity.noContent().build(); 
    }
}