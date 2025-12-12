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

import com.skillstorm.reliable_api.dtos.ProductDTO;
import com.skillstorm.reliable_api.dtos.ProductPatchDTO;
import com.skillstorm.reliable_api.dtos.ProductUpdateDTO;
import com.skillstorm.reliable_api.services.ProductService;

import jakarta.validation.Valid;


/**
 * REST controller for managing {@code Product} entities within the warehouse system.
 * This class provides standard CRUD (Create, Read, Update, Delete) operations
 * for product master data.
 * * @author Jevaughn Stewart
 * @version 1.0
 */
@RestController
@CrossOrigin(origins = {
    "http://localhost:5173",
    "https://main.d2ndcnwudegqtw.amplifyapp.com"
})
@RequestMapping("/api/warehouse/products")
public class ProductController {

    private final ProductService productService;

    /**
     * Constructs the ProductController, injecting the required ProductService.
     * * @param productService The service layer component responsible for product business logic.
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    /**
     * Handles the HTTP GET request to retrieve all active products.
     * * @return A {@code ResponseEntity} containing a list of all {@code ProductDTO} objects 
     * and an HTTP status of OK.
     */
    @GetMapping()
    public ResponseEntity<List<ProductDTO>> getProducts() {
     
    return ResponseEntity.ok(productService.getAllProducts());

    }
    
    /**
     * Handles the HTTP POST request to create a new product.
     * * @param product The {@code ProductDTO} containing the data for the new product.
     * @return A {@code ResponseEntity} containing the newly created {@code ProductDTO} and an 
     * HTTP status of OK.
     */
    @PostMapping()
    public ResponseEntity<ProductDTO> postProduct(@RequestBody ProductDTO product) {
        
        return new ResponseEntity<>( productService.createProduct(product),HttpStatus.OK);
    }
    
    /**
     * Handles the HTTP PATCH request to partially update an existing product identified by its public ID.
     * This allows for updating specific fields without requiring all product data.
     * * @param id The public ID of the product to update.
     * @param product The {@code ProductPatchDTO} containing only the fields to be updated.
     * @return A {@code ResponseEntity} containing the updated {@code ProductDTO} and an 
     * HTTP status of OK.
     */
    @PatchMapping("{id}")
     public ResponseEntity<ProductDTO> patchProduct(@PathVariable String id ,@RequestBody @Valid ProductPatchDTO product) {
        
        return new ResponseEntity<>( productService.patchProduct(id,product),HttpStatus.OK);
    }
    
    /**
     * Handles the HTTP PUT request to fully update an existing product identified by its public ID.
     * This typically replaces the entire product record with the new data.
     * * @param id The public ID of the product to update.
     * @param product The {@code ProductUpdateDTO} containing the full set of updated product data.
     * @return A {@code ResponseEntity} containing the updated {@code ProductDTO} and an 
     * HTTP status of OK.
     */
    @PutMapping("{id}")
    public ResponseEntity<ProductDTO> putProduct(@PathVariable String id, @RequestBody @Valid ProductUpdateDTO product) {
        
      return new ResponseEntity<>( productService.updateProduct(id,product),HttpStatus.OK);
    }
    
    /**
     * Handles the HTTP DELETE request to logically delete a product identified by its public ID.
     * * @param id The public ID of the product to delete.
     * @return A {@code ResponseEntity} with an HTTP status of 204 No Content upon successful deletion.
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}