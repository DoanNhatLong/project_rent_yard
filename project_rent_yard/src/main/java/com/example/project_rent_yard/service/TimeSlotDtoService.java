package com.example.project_rent_yard.service;

import com.example.project_rent_yard.dto.TimeSlotDto;
import com.example.project_rent_yard.entity.Booking;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TimeSlotDtoService implements ITimeSlotDtoService {

    private static final LocalTime OPEN_TIME = LocalTime.of(8, 0);
    private static final LocalTime CLOSE_TIME = LocalTime.of(22, 0);

    public List<TimeSlotDto> buildSlots(List<Booking> bookings) {

        LocalTime nowPlus1Hour = LocalDateTime.now().plusHours(1).toLocalTime();

        List<LocalTime> allSlots = new ArrayList<>();
        LocalTime t = OPEN_TIME;
        while (t.isBefore(CLOSE_TIME)) {
            allSlots.add(t);
            t = t.plusHours(1);
        }

        Set<LocalTime> booked = new HashSet<>();
        for (Booking b : bookings) {
            if (!b.getStatus().equals(Booking.BookingStatus.PENDING)
                    && !b.getStatus().equals(Booking.BookingStatus.COMPLETED)) {
                continue;
            }

            if (b.getStatus().equals(Booking.BookingStatus.PENDING)
                    && b.getExpiredAt().isBefore(LocalDateTime.now())) {
                continue;
            }
            LocalTime s = b.getStartTime();
            while (s.isBefore(b.getEndTime())) {
                booked.add(s);
                s = s.plusHours(1);
            }
        }

        List<TimeSlotDto> result = new ArrayList<>();
        for (LocalTime slot : allSlots) {

            boolean available =
                    !booked.contains(slot)      // chưa bị book
                            && slot.isAfter(nowPlus1Hour.minusNanos(1)); // >= now + 1h

            result.add(
                    new TimeSlotDto(
                            slot,
                            slot.plusHours(1),
                            available
                    )
            );
        }

        return result;
    }
}
