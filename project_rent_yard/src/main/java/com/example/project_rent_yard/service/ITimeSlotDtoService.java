package com.example.project_rent_yard.service;

import com.example.project_rent_yard.dto.TimeSlotDto;
import com.example.project_rent_yard.entity.Booking;

import java.util.List;

public interface ITimeSlotDtoService {
    List<TimeSlotDto> buildSlots(List<Booking> bookings);
}
