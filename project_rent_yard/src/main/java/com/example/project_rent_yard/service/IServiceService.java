package com.example.project_rent_yard.service;

import com.example.project_rent_yard.entity.Service;

import java.util.List;
import java.util.Set;

public interface IServiceService {
    List<Service> findAll();
    List<Service> findAllById(Set<Integer> id);
}
