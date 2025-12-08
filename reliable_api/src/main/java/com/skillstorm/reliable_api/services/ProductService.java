package com.skillstorm.reliable_api.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillstorm.reliable_api.dtos.ProductDTO;
import com.skillstorm.reliable_api.dtos.ProductPatchDTO;
import com.skillstorm.reliable_api.exceptions.ResourceNotFoundException;
import com.skillstorm.reliable_api.models.Category;
import com.skillstorm.reliable_api.models.Product; 
import com.skillstorm.reliable_api.repositories.CategoryRepo;
import com.skillstorm.reliable_api.repositories.ProductRepo;

@Service
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
    public List<ProductDTO> createProduct(List<ProductDTO>  requestDTO) {
        
        List<ProductDTO> productDTOs = requestDTO.stream().map(this::createHelper).collect(Collectors.toList());

     
    return productDTOs;
       
    }
private ProductDTO   createHelper(ProductDTO requestDTO){
 Product product = modelMapper.map(requestDTO, Product.class);
 if (requestDTO.getCategoryId() != null) {
            Category category = categoryRepo.findById(requestDTO.getCategoryId())
                // Use custom exception for proper 404 response
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + requestDTO.getCategoryId())); 
            
            product.setCategory(category);
        }
         Product savedProduct = productRepo.save(product);
return toDTO(savedProduct);

};


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

    // Map other non-null fields automatically
    modelMapper.map(dto, product);

    Product saved = productRepo.save(product);

    return toDTO(saved); 
}

@Transactional
public <T> ProductDTO updateProduct(String publicId, T dto) {

    Product product = productRepo.findByPublicIdAndIsDeletedFalse(publicId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

    //  Update category if dto has categoryId field
    try {
        Long categoryId = (Long) dto.getClass().getMethod("getCategoryId").invoke(dto);
        if (categoryId != null) {
            Category category = categoryRepo.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            product.setCategory(category);
        }
    } catch (NoSuchMethodException e) {
        // DTO does not have categoryId, ignore
    } catch (Exception e) {
        throw new RuntimeException("Error accessing DTO field", e);
    }

    modelMapper.map(dto, product);
    Product savedProduct = productRepo.save(product);
    return toDTO(savedProduct);
}


    public void deleteProduct(String id) {
        Product product = productRepo.findByPublicIdAndIsDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setIsDeleted(true);
        productRepo.save(product);
    }


}