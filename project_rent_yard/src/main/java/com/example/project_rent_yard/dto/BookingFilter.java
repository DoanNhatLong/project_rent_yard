package com.example.project_rent_yard.dto;

import com.example.project_rent_yard.entity.Booking;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class BookingFilter {
    private Booking.BookingStatus status;
    private String userName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate bookingDate;
}
