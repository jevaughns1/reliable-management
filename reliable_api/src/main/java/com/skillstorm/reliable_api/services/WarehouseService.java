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

@Service
@Transactional
public class WarehouseService {

    private final WarehouseRepo warehouseRepo;
    private final ModelMapper modelMapper;

    public WarehouseService(WarehouseRepo repo, ModelMapper mapper) {
        this.warehouseRepo = repo;
        this.modelMapper = mapper;
        this.modelMapper.getConfiguration().setSkipNullEnabled(true); 
    }

 
    public WarehouseDTO createWarehouse(WarehouseDTO dto) {
        Warehouse entity = modelMapper.map(dto, Warehouse.class);
        Warehouse saved = warehouseRepo.save(entity);
        return toDTO(saved);
    }

    public List<WarehouseDTO> getWarehouses() {
        return warehouseRepo.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

   
    public WarehouseDTO updateWarehouse(Long id, WarehouseUpdateDTO dto) {
        Warehouse existing = warehouseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));
        modelMapper.getConfiguration().setSkipNullEnabled(false); // overwrite all
        modelMapper.map(dto, existing);
        warehouseRepo.save(existing);
        return toDTO(existing);
    }


    public WarehouseDTO patchWarehouse(Long id, WarehousePatchDTO dto) {
        Warehouse existing = warehouseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));
        modelMapper.getConfiguration().setSkipNullEnabled(true); // skip nulls
        modelMapper.map(dto, existing);
        warehouseRepo.save(existing);
        return toDTO(existing);
    }

    private WarehouseDTO toDTO(Warehouse warehouse) {
        return modelMapper.map(warehouse, WarehouseDTO.class);
    }
    public void deleteWarehouse(Long id) {
        if (!warehouseRepo.existsById(id)) {
             throw new RuntimeException("Warehouse not found");
        }
        warehouseRepo.deleteById(id);
    }
}
