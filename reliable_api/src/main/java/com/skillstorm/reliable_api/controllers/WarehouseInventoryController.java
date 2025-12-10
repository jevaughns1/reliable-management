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


@RestController
@CrossOrigin(origins = {
    "http://localhost:5173",
    "https://main.d2ndcnwudegqtw.amplifyapp.com","http://18.219.1.224:8080"
})

@RequestMapping("/warehouses/inventory")
public class WarehouseInventoryController {

  

    private final WarehouseInventoryService inventoryService;

    public WarehouseInventoryController(WarehouseInventoryService inventoryService) {
        this.inventoryService = inventoryService;
     
    }

    @PostMapping("{warehouseId}")
    public ResponseEntity<WarehouseInventoryDTO> addProductToWarehouse(
            @PathVariable Long warehouseId,
            @RequestBody @Valid WarehouseInventoryCreateDTO dto) {

        WarehouseInventoryDTO savedInventory = inventoryService.addProductToWarehouse(warehouseId, dto);
        return  new ResponseEntity<>(savedInventory, HttpStatus.CREATED);
    }
    @GetMapping("/{warehouseId}")
    public ResponseEntity<WarehouseInventoryByWarehouseDTO> getInventoryByWarehouse(@PathVariable Long warehouseId) {
      return new ResponseEntity<>(inventoryService.getInventoryByWarehouse(warehouseId),HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<List<WarehouseInventoryDTO>> getAllInventory() {
      return new ResponseEntity<>(inventoryService.getAllWarehousesInventory(),HttpStatus.OK);
    }

@PostMapping("/transfer")
public ResponseEntity<Void> transferInventory(@RequestBody @Valid InventoryTransferDTO dto) {
    inventoryService.transferInventory(dto);
    return ResponseEntity.ok().build();
}

@DeleteMapping("/{warehouseId}/{productPublicId}")
public ResponseEntity<Void> deleteInventory(
        @PathVariable Long warehouseId,
        @PathVariable String productPublicId) {

    inventoryService.deleteInventoryFromWarehouse(warehouseId, productPublicId);
    return ResponseEntity.noContent().build();
}


}
