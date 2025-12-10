package com.skillstorm.reliable_api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

@RestController
@CrossOrigin(origins = {
    "http://localhost:5173",
    "https://main.d2ndcnwudegqtw.amplifyapp.com"
})
@RequestMapping("/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService service) {
        this.warehouseService = service;
    }

    @GetMapping
    public ResponseEntity<List<WarehouseDTO>> getWarehouses() {
        return ResponseEntity.ok(warehouseService.getWarehouses());
    }

    @PostMapping
    public ResponseEntity<WarehouseDTO> createWarehouse(@Valid @RequestBody WarehouseDTO dto) {
        return new ResponseEntity<>(warehouseService.createWarehouse(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseDTO> updateWarehouse(@PathVariable Long id,
                                                        @Valid @RequestBody WarehouseUpdateDTO dto) {
        return ResponseEntity.ok(warehouseService.updateWarehouse(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<WarehouseDTO> patchWarehouse(@PathVariable Long id,
                                                       @Valid @RequestBody WarehousePatchDTO dto) {
        return ResponseEntity.ok(warehouseService.patchWarehouse(id, dto));
    }
}
