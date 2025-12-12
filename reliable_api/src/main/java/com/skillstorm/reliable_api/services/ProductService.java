package com.skillstorm.reliable_api.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillstorm.reliable_api.dtos.ProductDTO;
import com.skillstorm.reliable_api.dtos.ProductPatchDTO;
import com.skillstorm.reliable_api.dtos.ProductUpdateDTO;
import com.skillstorm.reliable_api.exceptions.ResourceNotFoundException;
import com.skillstorm.reliable_api.models.Category;
import com.skillstorm.reliable_api.models.Product;
import com.skillstorm.reliable_api.repositories.CategoryRepo;
import com.skillstorm.reliable_api.repositories.ProductRepo;

/**
 * Service class responsible for handling business logic related to {@code Product} entities.
 * Manages CRUD operations, soft-deletion, and complex mapping between DTOs and the persistent entity model.
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
@Service
@Transactional
public class ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;

    /**
     * Constructs the ProductService with required dependencies and configures ModelMapper for product updates.
     * * @param productRepo The repository for accessing product data.
     * @param categoryRepo The repository for accessing category data.
     * @param modelMapper The utility for converting between DTOs and entities.
     */
    public ProductService(ProductRepo productRepo, CategoryRepo categoryRepo, ModelMapper modelMapper) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.modelMapper = modelMapper;

        // General Configuration for ModelMapper instance
        this.modelMapper.getConfiguration().setSkipNullEnabled(true);
        this.modelMapper.getConfiguration().setAmbiguityIgnored(true);
        
        // TypeMap specific configuration for full PUT updates (ProductUpdateDTO)
        // Ensure the internal primary key is never mapped/overwritten from the DTO.
        this.modelMapper.createTypeMap(ProductUpdateDTO.class, Product.class)
            .addMappings(mapper -> {
                // EXPLICITLY SKIP mapping the identifier field (productId)
                mapper.skip(Product::setProductId); 
            });
    }

    /**
     * Converts a {@code Product} entity to a {@code ProductDTO}.
     * Handles setting the category ID from the linked Category entity.
     * * @param entity The source {@code Product} entity.
     * @return The mapped {@code ProductDTO}.
     */
    private ProductDTO toDTO(Product entity) {
        ProductDTO dto = modelMapper.map(entity, ProductDTO.class);

        if (entity.getCategory() != null) {
            dto.setCategoryId(entity.getCategory().getId());
        }

        // Ensure the publicId is always set for API exposure
        dto.setPublicId(entity.getPublicId());
        return dto;
    }

    /**
     * Retrieves all active (non-deleted) products from the database.
     * * @return A list of all active {@code ProductDTO}s.
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        return productRepo.findAllByIsDeletedFalse().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new product. Links the product to an existing category based on {@code categoryId}.
     * * @param dto The {@code ProductDTO} containing the data for the new product.
     * @return The DTO of the newly created product.
     * @throws ResourceNotFoundException if the specified category ID does not exist.
     */
    @Transactional
    public  ProductDTO createProduct(ProductDTO dto) {

        Product entity = modelMapper.map(dto, Product.class);

        if (dto.getCategoryId() != null) {
            Category category = categoryRepo.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Category not found with ID: " + dto.getCategoryId()));

            entity.setCategory(category);
        }

        Product saved = productRepo.save(entity);
        return toDTO(saved);
    }

    /**
     * Performs a partial update (PATCH) on a product identified by its public ID.
     * Only non-null fields in the {@code ProductPatchDTO} will update the entity 
     * (due to {@code setSkipNullEnabled(true)} in the ModelMapper configuration).
     * * @param publicId The public ID of the product to update.
     * @param dto The {@code ProductPatchDTO} containing the fields to update.
     * @return The DTO of the partially updated product.
     * @throws ResourceNotFoundException if the product or the new category is not found.
     */
    @Transactional
    public ProductDTO patchProduct(String publicId, ProductPatchDTO dto) {

        Product product = productRepo.findByPublicIdAndIsDeletedFalse(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Update category if a new categoryId is provided in the DTO
        if (dto.getCategoryId() != null) {
            Category category = categoryRepo.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            product.setCategory(category);
        }

        // Use configured ModelMapper for PATCH: non-null fields only
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(dto, product);

        Product saved = productRepo.save(product);
        return toDTO(saved);
    }

    /**
     * Performs a full replacement update (PUT) on a product identified by its public ID.
     * All fields from the {@code ProductUpdateDTO} are mapped, overwriting existing values with 
     * nulls if the DTO fields were null (due to {@code setSkipNullEnabled(false)} for PUT).
     * * @param publicId The public ID of the product to update.
     * @param dto The {@code ProductUpdateDTO} containing the complete new data.
     * @return The DTO of the fully updated product.
     * @throws ResourceNotFoundException if the product or the category is not found.
     */
    @Transactional
    public ProductDTO updateProduct(String publicId, ProductUpdateDTO dto) {

        Product product = productRepo.findByPublicIdAndIsDeletedFalse(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Update category (required in PUT DTO)
        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        product.setCategory(category);

        // Temporarily configure ModelMapper for PUT MODE (do NOT skip nulls)
        modelMapper.getConfiguration().setSkipNullEnabled(false);
        // Note: The TypeMap configured in the constructor prevents productId from being set.
        modelMapper.map(dto, product);

        // Restore default configuration for other operations (though it should be reset on the modelMapper instance)
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        
        Product saved = productRepo.save(product);
        return toDTO(saved);
    }

    /**
     * Logically deletes (soft-deletes) a product identified by its public ID by setting {@code isDeleted = true}.
     * * @param publicId The public ID of the product to delete.
     * @throws ResourceNotFoundException if the product is not found.
     */
    @Transactional
    public void deleteProduct(String publicId) {
        Product product = productRepo.findByPublicIdAndIsDeletedFalse(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setIsDeleted(true);
        productRepo.save(product);
    }
}