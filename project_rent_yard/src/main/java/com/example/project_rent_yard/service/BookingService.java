package com.example.project_rent_yard.service;

import com.example.project_rent_yard.dto.SearchDto;
import com.example.project_rent_yard.entity.Booking;
import com.example.project_rent_yard.repository.IBookingRepository;
import com.example.project_rent_yard.specification.BookingSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    @Override
    public List<Integer> searchBusyField(SearchDto searchDto) {
        List<Booking> bookings=bookingRepository.findAll(BookingSpecification.busyField(searchDto));
        return bookings.stream()
                .map(b->b.getField().getId())
                .distinct()
                .toList()
                ;
    }

    @Override
    public List<Booking> findBookingsByBookingDateAndFieldId(LocalDate bookingDate, Integer field_id) {
        return bookingRepository.findBookingsByBookingDateAndFieldId(bookingDate,field_id);
    }
}
