package com.example.project_rent_yard.dto;

import com.example.project_rent_yard.entity.Field;
import com.example.project_rent_yard.validation.UniqueFieldName;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FieldCreateDto {
    @NotBlank(message = "Trường không được để trống")
    @Length(min = 3, max = 50, message = "Tên phải có độ dài từ 3 đến 50 ký tự")
    @UniqueFieldName
    private String name;
    @NotNull(message = "Trường không được để trống")
    @Min(value = 50000, message = "Giá phải lớn hơn 50000")
    private Double price;
    @NotBlank(message = "Trường không được để trống")
    private String imageUrl;
    @NotNull(message = "Trường không được để trống")
    private Field.FieldType fieldType;
    @NotNull(message = "Trường không được để trống")
    private Field.FieldStatus status;
}
