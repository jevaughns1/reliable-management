package com.skillstorm.reliable_api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


@RestController
@RequestMapping("api/categories")
public class CategoryController {

  

    private final CategoryService categoryService;
    

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
       
    }

@PostMapping
public ResponseEntity<CategoryDTO>postCategory(@RequestBody CategoryDTO category) {
 CategoryDTO createdCategory = categoryService.createCategory(category);
   
 return new ResponseEntity<>(createdCategory,HttpStatus.OK);
}
 
@GetMapping
public ResponseEntity<List<CategoryDTO>> getCategory() {
    List<CategoryDTO> categories= categoryService.getAllCategory();
    return new ResponseEntity<>(categories,HttpStatus.OK);
}

@PutMapping("/{id}")
public ResponseEntity<CategoryDTO> putCategory(@PathVariable Long id, @RequestBody @Valid CategoryUpdateDTO category) {
    
   CategoryDTO updatedCategory= categoryService.updateCategory(id,category);
    return new ResponseEntity<>(updatedCategory,HttpStatus.OK);
}

@PatchMapping("/{id}")
public ResponseEntity<CategoryDTO> patchCategory(@PathVariable Long id, @RequestBody @Valid CategoryPatchDTO category)
   {CategoryDTO updatedCategory= categoryService.updateCategory(id,category);
    return new ResponseEntity<>(updatedCategory,HttpStatus.OK);
}

@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        
        categoryService.deleteCategory(id);
        
        return ResponseEntity.noContent().build();
    }

    
}
