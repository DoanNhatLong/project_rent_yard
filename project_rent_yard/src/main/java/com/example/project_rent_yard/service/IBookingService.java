package com.example.project_rent_yard.service;

import com.example.project_rent_yard.dto.SearchDto;
import com.example.project_rent_yard.dto.ViewBookingDto;
import com.example.project_rent_yard.entity.Booking;
import com.example.project_rent_yard.entity.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface IBookingService {
    void save(Booking booking);
    List<Booking> findAll();
    Booking findBookingById(Integer id);
    List<Integer> searchBusyField(SearchDto searchDto);
    List<Booking> findBookingsByBookingDateAndFieldId(LocalDate bookingDate, Integer field_id);
    List<Booking> findBookingsByUser_Id(Integer userId);
    void createBooking(Booking booking);
    void updateBooking(Booking booking);
    Page<ViewBookingDto> getAll(Pageable pageable);
    Page<ViewBookingDto> getAllForAdmin(Pageable pageable, Booking.BookingStatus status, String userName, LocalDate bookingDate);
}
