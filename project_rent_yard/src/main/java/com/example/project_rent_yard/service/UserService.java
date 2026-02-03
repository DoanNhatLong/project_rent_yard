package com.example.project_rent_yard.service;

import com.example.project_rent_yard.entity.User;
import com.example.project_rent_yard.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;


    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
