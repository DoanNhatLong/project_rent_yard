package com.example.project_rent_yard.service;

import com.example.project_rent_yard.entity.Booking;
import com.example.project_rent_yard.entity.ServiceBooking;
import com.example.project_rent_yard.repository.IBookingRepository;
import com.example.project_rent_yard.repository.IServiceBookingRepository;
import com.example.project_rent_yard.repository.IServiceRepository;
import com.example.project_rent_yard.repository.projection.ServiceBookingView;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.project_rent_yard.entity.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceBookingService implements IServiceBookingService {
    @Autowired
    IServiceBookingRepository serviceBookingRepository;
    @Autowired
    IBookingRepository bookingRepository;
    @Autowired
    IServiceRepository serviceRepository;

    @Override
    public void createServiceBooking(Integer bookingID, Map<Integer, Integer> serviceMap) {
        Booking booking = bookingRepository.getReferenceById(bookingID);
        for(Map.Entry<Integer, Integer> entry : serviceMap.entrySet()){
            Integer serviceID = entry.getKey();
            Integer quantity = entry.getValue();
            Service service = serviceRepository.getReferenceById(serviceID);
            ServiceBooking serviceBooking=new ServiceBooking();
            serviceBooking.setBooking(booking);
            serviceBooking.setService(service);
            serviceBooking.setQuantity(quantity);
            serviceBookingRepository.save(serviceBooking);
        }

    }

    @Override
    public void save(ServiceBooking sb) {
        serviceBookingRepository.save(sb);
    }

    @Override
    public Optional<ServiceBooking> findByBookingIdAndServiceId(Integer bookingId, Integer serviceId) {
        return serviceBookingRepository.findByBookingIdAndServiceId(bookingId, serviceId);
    }

    @Override
    public List<ServiceBookingView> getAllServiceBookingViews() {
        return serviceBookingRepository.getAllServiceBookingViews();
    }
}
