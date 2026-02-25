package com.example.project_rent_yard.dto;

import com.example.project_rent_yard.entity.Booking;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookingHistoryDto {
    private Integer id;
    private Integer userId;
    private String fieldName;
    private double fieldPrice;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Booking.BookingStatus status;
}
