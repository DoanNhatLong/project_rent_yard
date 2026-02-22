package com.example.project_rent_yard.mapper;

import com.example.project_rent_yard.dto.ViewBookingDto;
import com.example.project_rent_yard.entity.Booking;
import com.example.project_rent_yard.entity.Field;
import com.example.project_rent_yard.entity.User;

public class BookingMapper {
    public static ViewBookingDto toViewBookingDto(Booking booking) {
        User user = booking.getUser();
        Field field = booking.getField();
        return ViewBookingDto.builder()
                .id(booking.getId())
                .userName(user.getName())
                .userPhone(user.getPhone())
                .fieldId(field.getId())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .status(booking.getStatus().name())
                .depositAmount(booking.getDepositAmount())
                .bookingDate(booking.getBookingDate())
                .build();
    }
}
