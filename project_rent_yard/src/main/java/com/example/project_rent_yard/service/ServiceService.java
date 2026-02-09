package com.example.project_rent_yard.service;

import com.example.project_rent_yard.entity.Service;
import com.example.project_rent_yard.repository.IServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

@org.springframework.stereotype.Service
public class ServiceService implements IServiceService {
    @Autowired
    IServiceRepository serviceRepository;

    @Override
    public List<Service> findAll() {
        return serviceRepository.findAll();
    }

    @Override
    public List<Service> findAllById(Set<Integer> id) {
        return serviceRepository.findAllById(id);
    }
}
