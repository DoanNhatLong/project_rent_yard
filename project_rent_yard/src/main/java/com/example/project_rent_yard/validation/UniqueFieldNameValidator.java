package com.example.project_rent_yard.validation;

import com.example.project_rent_yard.service.IFieldService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueFieldNameValidator implements ConstraintValidator<UniqueFieldName, String> {
    @Autowired
    IFieldService fieldService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        return !fieldService.existsByNameIgnoreCaseAndIsDeletedFalse(value);
    }
}
