package com.skillstorm.reliable_api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryDTO {
@NotBlank
@Size(max = 150)
private String name;
private String description;
private Long id;

public Long getId() {
    return id;
}
public void setId(Long id) {
    this.id = id;
} 

public String getName() {
    return name;
}
public void setName(String name) {
    this.name = name;
}
public String getDescription() {
    return description;
}
public void setDescription(String description) {
    this.description = description;
}


}
