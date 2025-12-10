package com.skillstorm.reliable_api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.skillstorm.reliable_api.models.Product;
import com.skillstorm.reliable_api.models.Warehouse;
import com.skillstorm.reliable_api.models.WarehouseInventory;

@Repository
public interface WarehouseInventoryRepo extends JpaRepository<WarehouseInventory, Long> {

    Optional<WarehouseInventory> findByWarehouseAndProduct(Warehouse warehouse, Product product);

    Optional<WarehouseInventory> findByProduct_ProductId(Long productId);
      @Query("SELECT wi FROM WarehouseInventory wi JOIN FETCH wi.warehouse w JOIN FETCH wi.product p")
    List<WarehouseInventory> findAllWithWarehouseAndProduct();
  @Query("SELECT wi FROM WarehouseInventory wi JOIN FETCH wi.product WHERE wi.warehouse.warehouseId = :warehouseId")
    List<WarehouseInventory> findAllWithProductsByWarehouseId(@Param("warehouseId") Long warehouseId);
    List<WarehouseInventory> findByWarehouse(Warehouse warehouse);
   List<WarehouseInventory> findAllByWarehouse_WarehouseId(Long warehouseId);
}
