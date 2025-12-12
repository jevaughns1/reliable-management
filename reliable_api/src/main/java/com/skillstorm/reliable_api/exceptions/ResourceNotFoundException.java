package com.skillstorm.reliable_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom runtime exception used to indicate that a requested resource (e.g., a Warehouse, Product, Category)
 * could not be found in the system.
 *
 * This exception is automatically translated by Spring into an HTTP 404 Not Found response
 * due to the {@code @ResponseStatus(HttpStatus.NOT_FOUND)} annotation.
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
// This annotation automatically maps this exception to an HTTP 404 response
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor that accepts a message detailing the resource that was not found.
     * * @param message The detail message (e.g., "Product with ID 123 not found").
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Optional constructor that accepts a message and an underlying cause.
     * * @param message The detail message.
     * @param cause The underlying cause of this exception.
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}