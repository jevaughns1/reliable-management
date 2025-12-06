package com.skillstorm.reliable_api.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.skillstorm.reliable_api.dtos.WarehouseUpdateDTO;
import com.skillstorm.reliable_api.models.Warehouse;
import com.skillstorm.reliable_api.repositories.WarehouseRepo;

import jakarta.transaction.Transactional;



@Service
public class WarehouseService {

    private final WarehouseRepo warehouseRepo;
    private final ModelMapper modelMapper;

    public WarehouseService(WarehouseRepo repo, ModelMapper mapper) {
        this.warehouseRepo = repo;
        this.modelMapper= mapper;

    }
    
    public Warehouse createWarehouse(Warehouse warehouse){
      
        return warehouseRepo.save(warehouse);

    }

@Transactional
public Warehouse patchWarehouse(Long id, WarehouseUpdateDTO dto) {
    Warehouse existing = warehouseRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Warehouse not found"));

    modelMapper.map(dto, existing); 
    return warehouseRepo.save(existing);
}

public List<Warehouse> getWarehouses(){
    List<Warehouse> allWarehouses = warehouseRepo.findAll();
    return allWarehouses;

}

public List<Warehouse> createWarehouses(List<Warehouse> warehouses){
    List<Warehouse> allWarehouses = warehouseRepo.saveAll(warehouses);

    return allWarehouses;
}

}