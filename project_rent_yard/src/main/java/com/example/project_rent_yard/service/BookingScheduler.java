package com.example.project_rent_yard.service;

import com.example.project_rent_yard.repository.IBookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingScheduler {

    private final IBookingRepository bookingRepository;

    @Scheduled(fixedRate = 60000) // mỗi 60 giây
    public void cancelExpiredBookings() {
        bookingRepository.cancelExpired(LocalDateTime.now());
    }
}
