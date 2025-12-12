package com.skillstorm.reliable_api.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


/**
 * Represents a product category entity that maps to the 'categories' table in the database.
 * This entity serves as the parent in a one-to-many relationship with the {@code Product} entity.
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
@Entity
@Table(name="categories")
public class Category {
    
    /**
     * The unique identifier (Primary Key) for the category.
     * Uses database identity generation strategy. Maps to the {@code category_id} column.
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="category_id")
    private Long id;
    
    /**
     * The name of the category. Maps to the {@code name} column.
     * Must be unique and non-null, with a maximum length of 150.
     */
    @Column(name = "name", length = 150, nullable = false, unique = true)
    private String name; 
    
    /**
     * A description of the category. Maps to the {@code description} column.
     * This field is optional (nullable), with a maximum length of 300.
     */
    @Column(name = "description", length = 300, nullable = true)
    private String description;
    
    /**
     * List of {@code Product} entities belonging to this category.
     * This establishes a One-to-Many relationship, mapped by the {@code category} field in the {@code Product} entity.
     * This side of the relationship is generally used for navigation and is not the owning side.
     */
     @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();
  
    /**
     * Generates a string representation of the Category entity.
     * * @return A string containing the field values.
     */
    @Override
    public String toString() {
        return "Category [id=" + id + ", name=" + name + ", description=" + description + ", products=" + products
                + "]";
    }
    
    /**
     * Generates a hash code for the Category entity, based on its fields.
     * * @return The hash code value.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((products == null) ? 0 : products.hashCode());
        return result;
    }
    
     /**
      * Compares this Category entity to the specified object. The result is true if and only if 
      * the argument is not null and is a Category object that has the same field values.
      * * @param obj The object to compare with.
      * @return {@code true} if the objects are the same; {@code false} otherwise.
      */
     @Override
     public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Category other = (Category) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (products == null) {
            if (other.products != null)
                return false;
        } else if (!products.equals(other.products))
            return false;
        return true;
     }
     
    /**
     * Default constructor required by JPA.
     */
    public Category() {
    }
    
    /**
     * Constructor for creating a Category with only a name.
     * * @param name The name of the category.
     */
    public Category(String name) {
        this.name = name;
    }
    
    /**
     * Constructor for creating a Category with name and description.
     * * @param name The name of the category.
     * @param description The description of the category.
     */
    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    /**
     * Retrieves the ID of the category.
     * * @return The category ID.
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Sets the ID of the category.
     * * @param id The new category ID.
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Retrieves the name of the category.
     * * @return The category name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the name of the category.
     * * @param name The new category name.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Retrieves the description of the category.
     * * @return The category description.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Sets the description of the category.
     * * @param description The new category description.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Retrieves the list of products associated with this category.
     * * @return The list of products.
     */
    public List<Product> getProducts() {
        return products;
    }
    
    /**
     * Sets the list of products associated with this category.
     * * @param products The new list of products.
     */
    public void setProducts(List<Product> products) {
        this.products = products;
    }
}