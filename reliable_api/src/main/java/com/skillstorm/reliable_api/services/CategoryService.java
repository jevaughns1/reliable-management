package com.skillstorm.reliable_api.services;


import java.util.LinkedList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.skillstorm.reliable_api.dtos.CategoryDTO;
import com.skillstorm.reliable_api.dtos.CategoryPatchDTO;
import com.skillstorm.reliable_api.exceptions.ResourceNotFoundException;
import com.skillstorm.reliable_api.models.Category;
import com.skillstorm.reliable_api.repositories.CategoryRepo;

import jakarta.transaction.Transactional;

@Service
public class CategoryService {

    private final CategoryRepo categoryRepo;
     private final ModelMapper modelMapper;
     
    public CategoryService(CategoryRepo categoryRepo, ModelMapper modelMapper) {
        this.categoryRepo = categoryRepo;
        this.modelMapper = modelMapper;
    }


@Transactional
public CategoryDTO createCategory( CategoryDTO categoryDTO) {
if(categoryRepo.existsByNameAllIgnoreCase(categoryDTO.getName())){
    throw new IllegalArgumentException("Category Already Exists"); 
}

       Category category= modelMapper.map(categoryDTO,Category.class);
       categoryRepo.save(category);
        return categoryDTO;
    }

    public List<CategoryDTO> getAllCategory(){

    List<Category> categories = categoryRepo.findAllByOrderByNameAsc();
    List<CategoryDTO> categoriesDTOs= new LinkedList<>();
    for (Category category : categories) {

        categoriesDTOs.add(modelMapper.map(category,CategoryDTO.class));
    }

     return categoriesDTOs; 
    }

 @Transactional   
public <T>CategoryDTO updateCategory(Long id, T categoryDTO){
 
    Category category = categoryRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category not found with ID: " + id));
    
    modelMapper.map(categoryDTO,category);
   
    Category updatedCategory = categoryRepo.save(category);
 
    return modelMapper.map(updatedCategory,CategoryDTO.class );
}

@Transactional
    public CategoryDTO patchCategory(Long id, CategoryPatchDTO categoryDTO) {
        // Find existing resource, throwing 404 if not found
        Category existingCategory = categoryRepo.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Category not found with ID: " + id)
        );
        
        // Map the partial DTO onto the existing entity.
        // Because SkipNullEnabled is TRUE, ONLY non-null fields from the DTO are copied.
        modelMapper.map(categoryDTO, existingCategory); 
        
        Category updatedEntity = categoryRepo.save(existingCategory);
        return toDTO(updatedEntity);
    }

    public void deleteCategory(Long id){
        categoryRepo.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Category not found with ID: " + id)
        );
    
        categoryRepo.deleteById(id);
    }

    private CategoryDTO toDTO(Category entity) {
        return modelMapper.map(entity, CategoryDTO.class);
    }

    // Converts Create Request DTO to Entity
    /*
     private Category toEntity(CategoryDTO dto) {
        return modelMapper.map(dto, Category.class);
    } */
   
    
}
