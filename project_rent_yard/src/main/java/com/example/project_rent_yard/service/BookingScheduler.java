package com.example.project_rent_yard.service;

import com.example.project_rent_yard.repository.IBookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class BookingScheduler {

    private final IBookingRepository bookingRepository;

    @Scheduled(fixedRate = 600000)
    public void cancelExpiredBookings() {
        bookingRepository.cancelExpired(LocalDateTime.now());
    }
}
