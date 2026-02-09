package com.example.project_rent_yard.service;

import com.example.project_rent_yard.entity.Booking;
import com.example.project_rent_yard.repository.IBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService implements IBookingService {
    @Autowired
    IBookingRepository bookingRepository;


    @Override
    public void save(Booking booking) {
        bookingRepository.save(booking);
    }

    @Override
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking findBookingById(Integer id) {
        return bookingRepository.findBookingById(id);
    }
}
