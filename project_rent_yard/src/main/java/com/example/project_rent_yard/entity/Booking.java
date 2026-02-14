package com.example.project_rent_yard.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "booking",
uniqueConstraints = {
        @UniqueConstraint(columnNames = {"field_id", "booking_date", "start_time"} )
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "is_deleted = 0")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "field_id", nullable = false)
    private Field field;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    private BookingStatus status= BookingStatus.PENDING;

    private double depositAmount;

    private LocalTime expiredAt=LocalTime.now();

    private boolean isDeleted=false;

    private LocalDate bookingDate;

    public enum BookingStatus {
        BOOKED,
        CANCELLED_BY_CUSTOMER,
        CANCELLED_BY_OWNER,
        COMPLETED,
        PENDING
    }
}

