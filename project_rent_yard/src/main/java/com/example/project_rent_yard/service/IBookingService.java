package com.example.project_rent_yard.service;

import com.example.project_rent_yard.dto.SearchDto;
import com.example.project_rent_yard.entity.Booking;
import com.example.project_rent_yard.entity.Field;

import java.time.LocalDate;
import java.util.List;

public interface IBookingService {
    void save(Booking booking);
    List<Booking> findAll();
    Booking findBookingById(Integer id);
    List<Integer> searchBusyField(SearchDto searchDto);
    List<Booking> findBookingsByBookingDateAndFieldId(LocalDate bookingDate, Integer field_id);
}
