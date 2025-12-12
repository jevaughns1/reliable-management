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

/**
 * Service class responsible for handling business logic related to {@code Category} entities.
 * This includes CRUD (Create, Read, Update, Delete) operations, data validation, 
 * and conversion between DTOs and the persistent entity model using {@code ModelMapper}.
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
@Service
public class CategoryService {

    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;
     
    /**
     * Constructs the CategoryService with required dependencies.
     * * @param categoryRepo The repository for accessing category data.
     * @param modelMapper The utility for converting between DTOs and entities.
     */
    public CategoryService(CategoryRepo categoryRepo, ModelMapper modelMapper) {
        this.categoryRepo = categoryRepo;
        this.modelMapper = modelMapper;
    }


    /**
     * Creates a new {@code Category} in the database.
     * Enforces that the category name is unique (case-insensitive).
     * * @param categoryDTO The DTO containing the data for the new category.
     * @return The DTO of the newly created category.
     * @throws IllegalArgumentException if a category with the same name already exists.
     */
    @Transactional
    public CategoryDTO createCategory( CategoryDTO categoryDTO) {
        if(categoryRepo.existsByNameAllIgnoreCase(categoryDTO.getName())){
            throw new IllegalArgumentException("Category Already Exists"); 
        }

       Category category = modelMapper.map(categoryDTO, Category.class);
       categoryRepo.save(category);
        return categoryDTO;
    }

    /**
     * Retrieves all categories, ordered alphabetically by name.
     * Converts the list of {@code Category} entities to a list of {@code CategoryDTO}s.
     * * @return A list of all categories as DTOs, sorted by name.
     */
    public List<CategoryDTO> getAllCategory(){

        List<Category> categories = categoryRepo.findAllByOrderByNameAsc();
        List<CategoryDTO> categoriesDTOs = new LinkedList<>();
        for (Category category : categories) {
            categoriesDTOs.add(modelMapper.map(category, CategoryDTO.class));
        }

        return categoriesDTOs; 
    }

    /**
     * Updates an existing category fully (PUT semantics).
     * This method is generic to support full updates using different DTOs if necessary.
     * * @param id The ID of the category to update.
     * @param categoryDTO The DTO containing the new data.
     * @param <T> The type of the DTO (e.g., {@code CategoryDTO}).
     * @return The DTO of the updated category.
     * @throws ResourceNotFoundException if the category with the given ID does not exist.
     */
    @Transactional   
    public <T>CategoryDTO updateCategory(Long id, T categoryDTO){
     
        Category category = categoryRepo.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Category not found with ID: " + id)
        );
        
        // Maps all fields from categoryDTO onto the existing category entity
        modelMapper.map(categoryDTO, category);
       
        Category updatedCategory = categoryRepo.save(category);
     
        return modelMapper.map(updatedCategory, CategoryDTO.class );
    }

    /**
     * Performs a partial update on an existing category (PATCH semantics).
     * Only non-null fields from the {@code CategoryPatchDTO} are copied to the entity, 
     * thanks to the {@code ModelMapper} configuration.
     * * @param id The ID of the category to update.
     * @param categoryDTO The DTO containing the fields to update.
     * @return The DTO of the partially updated category.
     * @throws ResourceNotFoundException if the category with the given ID does not exist.
     */
    @Transactional
    public CategoryDTO patchCategory(Long id, CategoryPatchDTO categoryDTO) {
        // Find existing resource, throwing 404 if not found
        Category existingCategory = categoryRepo.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Category not found with ID: " + id)
        );
        
        // Map the partial DTO onto the existing entity.
        // Because SkipNullEnabled is TRUE in ModelMapperConfig, ONLY non-null fields from the DTO are copied.
        modelMapper.map(categoryDTO, existingCategory); 
        
        Category updatedEntity = categoryRepo.save(existingCategory);
        return toDTO(updatedEntity);
    }

    /**
     * Deletes a category by its ID.
     * * @param id The ID of the category to delete.
     * @throws ResourceNotFoundException if the category with the given ID does not exist.
     */
    public void deleteCategory(Long id){
        categoryRepo.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Category not found with ID: " + id)
        );
    
        categoryRepo.deleteById(id);
    }

    /**
     * Private helper method to convert a {@code Category} entity to a {@code CategoryDTO}.
     * * @param entity The source entity.
     * @return The mapped DTO.
     */
    private CategoryDTO toDTO(Category entity) {
        return modelMapper.map(entity, CategoryDTO.class);
    }

  
    
}