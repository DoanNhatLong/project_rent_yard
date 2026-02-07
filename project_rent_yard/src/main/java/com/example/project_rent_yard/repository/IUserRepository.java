package com.example.project_rent_yard.repository;

import com.example.project_rent_yard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
    boolean existsByPhone(String phone);

}
