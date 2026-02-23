package com.example.project_rent_yard.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueFieldNameValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueFieldName {
    String message() default "Tên sân đã tồn tại";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
}
