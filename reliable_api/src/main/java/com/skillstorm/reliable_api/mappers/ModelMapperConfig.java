package com.skillstorm.reliable_api.mappers;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
       
ModelMapper modelMapper = new ModelMapper();
modelMapper.getConfiguration()
    .setPropertyCondition(Conditions.isNotNull())
    .setSkipNullEnabled(true);

        return modelMapper;
    }
}
