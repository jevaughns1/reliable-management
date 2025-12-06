package com.skillstorm.reliable_api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.reliable_api.dtos.WarehouseUpdateDTO;
import com.skillstorm.reliable_api.models.Warehouse;
import com.skillstorm.reliable_api.services.WarehouseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {
    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService service){
        this.warehouseService= service; 
    }


    @PostMapping()
    public ResponseEntity<Warehouse> postMethodName(@Valid @RequestBody Warehouse entity) {
       Warehouse createdWarehouse = warehouseService.createWarehouse(entity);
       return new ResponseEntity<>(createdWarehouse, HttpStatus.CREATED);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Warehouse>  patchWarehouse(@PathVariable Long id,
        @RequestBody WarehouseUpdateDTO entityUpdateDTO ){
            Warehouse temp= warehouseService.patchWarehouse(id,entityUpdateDTO);
            return new ResponseEntity<>(temp, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Warehouse>>  getWarehouses() {
        List<Warehouse> allWarehouses = warehouseService.getWarehouses();

        return new ResponseEntity<>(allWarehouses,HttpStatus.OK);
    }

    @PostMapping("all")
    public  ResponseEntity<List<Warehouse>>
    createWarehouse(@RequestBody List<Warehouse> warehouses) {
        List<Warehouse> allWarehouses = warehouseService.createWarehouses(warehouses);
        
    return new ResponseEntity<>(allWarehouses,HttpStatus.OK);
    }
    
    
}
