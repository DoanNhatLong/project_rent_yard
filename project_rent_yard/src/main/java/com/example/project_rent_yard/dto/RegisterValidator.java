package com.example.project_rent_yard.dto;

import com.example.project_rent_yard.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RegisterValidator implements Validator {
    @Autowired
    private IUserService userService;
    @Override
    public boolean supports(Class<?> clazz) {
        return RegisterDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegisterDto registerDto = (RegisterDto) target;
        if (!errors.hasFieldErrors("name")) {
            String name = registerDto.getName();
            if (!name.matches("^[A-Za-z0-9]+$")) {
                errors.rejectValue(
                        "name",
                        "register.name.invalidChar"
                );
            } else if (!Character.isUpperCase(name.charAt(0))) {
                errors.rejectValue(
                        "name",
                        "register.name.firstUppercase"
                );
            }
        }
        if (!errors.hasFieldErrors("password")) {
            if (registerDto.getPassword().length() < 6) {
                errors.rejectValue(
                        "password",
                        "register.password.length"
                );
            }
        }
        if (!errors.hasFieldErrors("password")
                && !errors.hasFieldErrors("confirmPassword")) {

            if (!registerDto.getPassword()
                    .equals(registerDto.getConfirmPassword())) {

                errors.rejectValue(
                        "confirmPassword",
                        "register.password.notMatch"
                );
            }
        }
        if (!errors.hasFieldErrors("phone")) {
            String phone = registerDto.getPhone();
            if (!phone.matches("^0[0-9]{9}$")) {
                errors.rejectValue(
                        "phone",
                        "register.phone.invalidFormat"
                );
            } else if (userService.existsByPhone(phone)) {
                errors.rejectValue(
                        "phone",
                        "register.phone.exists"
                );
            }
        }
    }
}
