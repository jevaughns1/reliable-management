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




@RestController
@CrossOrigin(origins = {
    "http://localhost:5173",
    "https://main.d2ndcnwudegqtw.amplifyapp.com"
})
@RequestMapping("/api/warehouse/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping()
    public ResponseEntity<List<ProductDTO>> getProducts() {
     
    return ResponseEntity.ok(productService.getAllProducts());

    }
    @PostMapping()
    public ResponseEntity<ProductDTO> postProduct(@RequestBody ProductDTO product) {
        
        return new ResponseEntity<>( productService.createProduct(product),HttpStatus.OK);
    }
    @PatchMapping("{id}")
     public ResponseEntity<ProductDTO> patchProduct(@PathVariable String id ,@RequestBody @Valid ProductPatchDTO product) {
        
        return new ResponseEntity<>( productService.patchProduct(id,product),HttpStatus.OK);
    }
    @PutMapping("{id}")
    public ResponseEntity<ProductDTO> putProduct(@PathVariable String id, @RequestBody @Valid ProductUpdateDTO product) {
        
      return new ResponseEntity<>( productService.updateProduct(id,product),HttpStatus.OK);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
