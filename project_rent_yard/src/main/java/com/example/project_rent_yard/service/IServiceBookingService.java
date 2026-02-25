package com.example.project_rent_yard.service;

import com.example.project_rent_yard.entity.ServiceBooking;
import com.example.project_rent_yard.repository.projection.ServiceBookingView;
import com.example.project_rent_yard.repository.projection.ServiceStatisticView;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IServiceBookingService {
    void createServiceBooking(Integer bookingID, Map<Integer,Integer> cart);
    void save(ServiceBooking sb);
    Optional<ServiceBooking> findByBookingIdAndServiceId(Integer bookingId, Integer serviceId);
    List<ServiceBookingView> getAllServiceBookingViews();
    List<ServiceStatisticView> getServiceStatistics();
}
