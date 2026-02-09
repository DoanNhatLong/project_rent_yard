package com.example.project_rent_yard.repository;

import com.example.project_rent_yard.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IServiceRepository extends JpaRepository<Service,Integer> {
    List<Service> findAllById(Integer id);
}
