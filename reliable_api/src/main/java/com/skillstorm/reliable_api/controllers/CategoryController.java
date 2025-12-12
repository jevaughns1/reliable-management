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

import com.skillstorm.reliable_api.dtos.CategoryDTO;
import com.skillstorm.reliable_api.dtos.CategoryPatchDTO;
import com.skillstorm.reliable_api.dtos.CategoryUpdateDTO;
import com.skillstorm.reliable_api.services.CategoryService;

import jakarta.validation.Valid;


/**
 * REST controller for managing product categories.
 * Provides endpoints for creating, retrieving, updating (full and partial),
 * and deleting categories.
 * * @author Jevaughn Stewart
 * @version 1.0
 */
@RestController
@CrossOrigin(origins = {
    "http://localhost:5173",
    "https://main.d2ndcnwudegqtw.amplifyapp.com"
})
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    
    /**
     * Constructs the CategoryController, injecting the required CategoryService.
     * * @param categoryService The service layer component responsible for category business logic.
     */
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
       
    }

/**
 * Handles the HTTP POST request to create a new product category.
 * * @param category The CategoryDTO containing the data for the new category.
 * @return A ResponseEntity containing the newly created CategoryDTO and an HTTP status of OK.
 */
@PostMapping
public ResponseEntity<CategoryDTO>postCategory(@RequestBody CategoryDTO category) {
 CategoryDTO createdCategory = categoryService.createCategory(category);
   
 return new ResponseEntity<>(createdCategory,HttpStatus.OK);
}
 
/**
 * Handles the HTTP GET request to retrieve all product categories.
 * * @return A ResponseEntity containing a list of all CategoryDTOs and an HTTP status of OK.
 */
@GetMapping
public ResponseEntity<List<CategoryDTO>> getCategory() {
    List<CategoryDTO> categories= categoryService.getAllCategory();
    return new ResponseEntity<>(categories,HttpStatus.OK);
}

/**
 * Handles the HTTP PUT request to fully update an existing product category.
 * * @param id The ID of the category to update.
 * @param category The CategoryUpdateDTO containing the full set of updated category data.
 * @return A ResponseEntity containing the updated CategoryDTO and an HTTP status of OK.
 */
@PutMapping("/{id}")
public ResponseEntity<CategoryDTO> putCategory(@PathVariable Long id, @RequestBody @Valid CategoryUpdateDTO category) {
    
   CategoryDTO updatedCategory= categoryService.updateCategory(id,category);
    return new ResponseEntity<>(updatedCategory,HttpStatus.OK);
}

/**
 * Handles the HTTP PATCH request to partially update an existing product category.
 * * @param id The ID of the category to partially update.
 * @param category The CategoryPatchDTO containing the fields to be updated.
 * @return A ResponseEntity containing the updated CategoryDTO and an HTTP status of OK.
 */
@PatchMapping("/{id}")
public ResponseEntity<CategoryDTO> patchCategory(@PathVariable Long id, @RequestBody @Valid CategoryPatchDTO category)
   {CategoryDTO updatedCategory= categoryService.updateCategory(id,category);
    return new ResponseEntity<>(updatedCategory,HttpStatus.OK);
}

/**
 * Handles the HTTP DELETE request to delete a product category by its ID.
 * * @param id The ID of the category to delete.
 * @return A ResponseEntity with an HTTP status of 204 No Content upon successful deletion.
 */
@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        
        categoryService.deleteCategory(id);
        
        return ResponseEntity.noContent().build();
    }

    
}