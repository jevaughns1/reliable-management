package com.skillstorm.reliable_api.mappers;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up and customizing the ModelMapper bean.
 * ModelMapper is used throughout the application to map properties between 
 * Data Transfer Objects (DTOs) and persistent Entities (Models).
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
@Configuration
public class ModelMapperConfig {

    /**
     * Defines the ModelMapper bean with custom configuration for property mapping.
     * <p>
     * The configuration ensures that:
     * <ul>
     * <li>Only non-null properties from the source (e.g., a DTO) are mapped to the destination (e.g., an Entity).</li>
     * <li>This is crucial for PATCH operations (like {@code WarehousePatchDTO}) where missing (null) fields 
     * in the DTO should not overwrite existing values in the database entity.</li>
     * </ul>
     * </p>
     * * @return A configured {@code ModelMapper} instance.
     */
    @Bean
    public ModelMapper modelMapper() {
       
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
            // 1. Sets a condition to only map properties if the source value is not null.
            .setPropertyCondition(Conditions.isNotNull())
            // 2. Explicitly enables skipping null values, enforcing the condition above.
            .setSkipNullEnabled(true);

        return modelMapper;
    }
}