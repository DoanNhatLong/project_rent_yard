package com.example.project_rent_yard.service;

import com.example.project_rent_yard.entity.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IFieldService {
    Page<Field> findAll(Pageable pageable);
    Page<Field> showFields(
            Field.FieldType fieldType,
            Field.FieldStatus fieldStatus,
            Double minPrice,
            Double maxPrice,
            Pageable pageable);

    Field findById(Integer id);
    List<Field> findAll();
    void save(Field field);
}
