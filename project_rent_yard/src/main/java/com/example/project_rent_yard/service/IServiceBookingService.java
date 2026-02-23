package com.example.project_rent_yard.service;

import java.util.Map;

public interface IServiceBookingService {
    void createServiceBooking(Integer bookingID, Map<Integer,Integer> cart);
}
