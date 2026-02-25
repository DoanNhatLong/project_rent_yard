package com.example.project_rent_yard.repository.projection;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ServiceBookingView {
    Integer getBookingId();
    String getFieldName();
    Double getFieldPrice();
    LocalDate getBookingDate();
    LocalTime getStartTime();
    LocalTime getEndTime();
    Double getTotalServiceAmount();
}
