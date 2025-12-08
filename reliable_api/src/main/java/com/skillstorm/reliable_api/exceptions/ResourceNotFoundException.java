
package com.skillstorm.reliable_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// This annotation automatically maps this exception to an HTTP 404 response
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    // Constructor that accepts a message
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Optional: Constructor that accepts a message and cause
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}