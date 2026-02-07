package com.example.project_rent_yard.service;

import com.example.project_rent_yard.entity.Field;
import com.example.project_rent_yard.repository.IFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FieldService implements IFieldService {
    @Autowired
    private IFieldRepository fieldRepository;


    @Override
    public Page<Field> findAll(Pageable pageable) {
        return fieldRepository.findAll(pageable);
    }

    @Override
    public Page<Field> showFields(Field.FieldType fieldType, Field.FieldStatus fieldStatus, Double minPrice, Double maxPrice, Pageable pageable) {
        return fieldRepository.showFields(fieldType, fieldStatus, minPrice, maxPrice, pageable);
    }

    @Override
    public Field findById(Integer id) {
        return fieldRepository.findById(id).orElse(null);
    }
}
