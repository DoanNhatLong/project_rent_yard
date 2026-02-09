package com.example.project_rent_yard.service;

public interface IVnPayService {
    String createVnPayUrl(Integer bookingId, double amount);
}

