package com.example.project_rent_yard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {
    @NotBlank(message = "Tên không được để trống")
    @Size(min = 3, max = 50, message = "Tên phải có độ dài từ 3 đến 50 ký tự")
    private String name;

    @NotBlank(message = "Điện thoại không được để trống")
    @Pattern(
            regexp = "^0\\d{9}$",
            message = "Số điện thoại không hợp lệ"
    )
    private String phone;
    private String password;
}
