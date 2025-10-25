package com.profiletool.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

/**
 * Service for processing validation errors from a BindingResult.
 */
@Service
public class ValidationService {

    /**
     * Converts a BindingResult's field errors into a simple map of field names to error messages.
     *
     * @param bindingResult The BindingResult from a Spring MVC controller.
     * @return A map where the key is the field name and the value is the default error message.
     */
    public Map<String, String> getErrors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }
}
