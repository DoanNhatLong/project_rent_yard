package com.example.project_rent_yard.service;

import com.example.project_rent_yard.dto.UserDto;
import com.example.project_rent_yard.entity.User;

import java.util.List;

public interface IUserService {
    List<User> findAll();
    UserDto findDtoById(Integer id);
}
