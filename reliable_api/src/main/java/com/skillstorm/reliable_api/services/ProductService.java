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

@Service
@Transactional
public class ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;

    public ProductService(ProductRepo productRepo, CategoryRepo categoryRepo, ModelMapper modelMapper) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.modelMapper = modelMapper;

        this.modelMapper.getConfiguration().setSkipNullEnabled(true);
        this.modelMapper.getConfiguration().setAmbiguityIgnored(true);
    }

   
    private ProductDTO toDTO(Product entity) {
        ProductDTO dto = modelMapper.map(entity, ProductDTO.class);

        if (entity.getCategory() != null) {
            dto.setCategoryId(entity.getCategory().getId());
        }

        dto.setPublicId(entity.getPublicId());
        return dto;
    }

    
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        return productRepo.findAllByIsDeletedFalse().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    
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

 
    @Transactional
    public ProductDTO patchProduct(String publicId, ProductPatchDTO dto) {

        Product product = productRepo.findByPublicIdAndIsDeletedFalse(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Update category if provided
        if (dto.getCategoryId() != null) {
            Category category = categoryRepo.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            product.setCategory(category);
        }

       
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(dto, product);

        Product saved = productRepo.save(product);
        return toDTO(saved);
    }


    @Transactional
    public ProductDTO updateProduct(String publicId, ProductUpdateDTO dto) {

        Product product = productRepo.findByPublicIdAndIsDeletedFalse(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Update category (required in PUT)
        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        product.setCategory(category);

        // PUT MODE (do NOT skip nulls)
        modelMapper.getConfiguration().setSkipNullEnabled(false);
        modelMapper.map(dto, product);

        Product saved = productRepo.save(product);
        return toDTO(saved);
    }

    @Transactional
    public void deleteProduct(String publicId) {
        Product product = productRepo.findByPublicIdAndIsDeletedFalse(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setIsDeleted(true);
        productRepo.save(product);
    }
}
