package com.example.project_rent_yard.service;

import com.example.project_rent_yard.dto.UserDto;
import com.example.project_rent_yard.entity.Booking;
import com.example.project_rent_yard.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface IUserService {
    List<User> findAll();
    UserDto findDtoById(Integer id);
    boolean existsByPhone(String phone);
    User findUserById(Integer id);
    void save(User user);

}
