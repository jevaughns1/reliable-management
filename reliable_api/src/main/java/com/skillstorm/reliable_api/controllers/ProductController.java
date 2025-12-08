package com.skillstorm.reliable_api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("api/warehouse/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping()
    public ResponseEntity<List<ProductDTO>> getProducts() {
        productService.getAllProducts();
        return new ResponseEntity<>( productService.getAllProducts(),HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<List<ProductDTO>> postProduct(@RequestBody List<ProductDTO> product) {
        
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
}
