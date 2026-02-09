package com.example.project_rent_yard.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    @NotBlank (message = "Trường không được để trống")
    String name;
    @NotBlank (message = "Trường không được để trống")
    String phone;
    @NotBlank (message = "Trường không được để trống")
    String password;
    String confirmPassword;

}
