package com.example.project_rent_yard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchDto {
    private String fieldType;
    private LocalDate bookingDate;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @Override
    public String toString() {
        return "SearchDto{" +
                "fieldType='" + fieldType + '\'' +
                ", bookingDate=" + bookingDate +
                ", startTime=" + startTime +
                ", endTIme=" + endTime +
                '}';
    }
}
