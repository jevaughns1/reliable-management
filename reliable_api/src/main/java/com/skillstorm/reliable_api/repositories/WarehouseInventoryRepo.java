package com.skillstorm.reliable_api.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.skillstorm.reliable_api.models.Product;
import com.skillstorm.reliable_api.models.Warehouse;
import com.skillstorm.reliable_api.models.WarehouseInventory;

/**
 * Repository interface for managing {@code WarehouseInventory} entities.
 * Extends {@code JpaRepository} and defines custom query methods essential for 
 * inventory management, including lookup by composite key (warehouse and product), 
 * eager fetching, and expiration date filtering.
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
@Repository
public interface WarehouseInventoryRepo extends JpaRepository<WarehouseInventory, Long> {

    /**
     * Retrieves an {@code Optional} containing a {@code WarehouseInventory} record 
     * based on the unique combination of the {@code Warehouse} and {@code Product} entities.
     * This effectively acts as a lookup by the composite unique key.
     * * @param warehouse The warehouse entity.
     * @param product The product entity.
     * @return An {@code Optional} containing the inventory record, or {@code Optional.empty()} if not found.
     */
    Optional<WarehouseInventory> findByWarehouseAndProduct(Warehouse warehouse, Product product);

    /**
     * Finds a single {@code WarehouseInventory} record by the internal primary key of the product.
     * This assumes a product ID will only correspond to one inventory record, which may not be the case 
     * if a product is stocked in multiple warehouses. Use {@code findByWarehouseAndProduct} for uniqueness.
     * * @param productId The internal primary key of the product.
     * @return An {@code Optional} containing the inventory record.
     */
    Optional<WarehouseInventory> findByProduct_ProductId(Long productId);
    
    /**
     * Retrieves all {@code WarehouseInventory} records using a JPQL query with JOIN FETCH 
     * to eagerly load the associated {@code Warehouse} and {@code Product} entities.
     * This avoids N+1 query problems when accessing these relationships.
     * * @return A list of all inventory records with their warehouse and product details eagerly loaded.
     */
    @Query("SELECT wi FROM WarehouseInventory wi JOIN FETCH wi.warehouse w JOIN FETCH wi.product p")
    List<WarehouseInventory> findAllWithWarehouseAndProduct();

    /**
     * Retrieves all {@code WarehouseInventory} records for a specific warehouse ID, 
     * eagerly fetching the associated {@code Product} details.
     * It only includes inventory where the associated product has not been logically deleted (soft-delete filter).
     * * @param warehouseId The ID of the warehouse to filter by.
     * @return A list of active inventory records for the specified warehouse.
     */
    @Query("SELECT wi FROM WarehouseInventory wi JOIN FETCH wi.product p " +
           "WHERE wi.warehouse.warehouseId = :warehouseId " +
           "AND p.isDeleted = FALSE") 
    List<WarehouseInventory> findAllWithProductsByWarehouseIdAndIsDeletedFalse(@Param("warehouseId") Long warehouseId);
    
    /**
     * Retrieves all {@code WarehouseInventory} records associated with a given {@code Warehouse} entity.
     * * @param warehouse The warehouse entity.
     * @return A list of inventory records for the specified warehouse.
     */
    List<WarehouseInventory> findByWarehouse(Warehouse warehouse);
    
    /**
     * Retrieves all {@code WarehouseInventory} records associated with a given warehouse ID.
     * * @param warehouseId The ID of the warehouse.
     * @return A list of inventory records for the specified warehouse ID.
     */
   List<WarehouseInventory> findAllByWarehouse_WarehouseId(Long warehouseId);

   /**
    * Retrieves all inventory records whose expiration date falls within a specified date range (inclusive).
    * * @param dateFrom The start date of the range.
    * @param dateTo The end date of the range.
    * @return A list of inventory records expiring between the two dates.
    */
   List<WarehouseInventory> findByExpirationDateBetween(LocalDate dateFrom, LocalDate dateTo);

    /**
     * Retrieves all inventory records whose expiration date is before the specified date.
     * This is useful for identifying inventory that is already expired or nearing expiration.
     * * @param date The date to check against (exclusive).
     * @return A list of inventory records with an expiration date before the given date.
     */
    List<WarehouseInventory> findByExpirationDateBefore(LocalDate date);
}