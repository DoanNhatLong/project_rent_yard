package com.example.project_rent_yard.service;

import com.example.project_rent_yard.entity.ServiceBooking;

import java.util.Map;

public interface IServiceBookingService {
    void createServiceBooking(Integer bookingID, Map<Integer,Integer> cart);

    void save(ServiceBooking sb);
}
