package com.example.letscode.Controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collectors;

public class ControllerUtils {

    static Map<String, String> getErrorsMap(BindingResult bindingResult) {
        Map<String, String> errorsMap = bindingResult.getFieldErrors().stream().collect(Collectors.toMap(
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage
        ));
        return errorsMap;
    }


}
