package com.example.project_rent_yard.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "booking")
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

    private LocalTime startTime;

    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    private BookingStatus status= BookingStatus.PENDING;

    private double depositAmount;

    private boolean isDeleted=false;

    private LocalDateTime bookingDate;

    public enum BookingStatus {
        BOOKED,
        CANCELLED_BY_CUSTOMER,
        CANCELLED_BY_OWNER,
        COMPLETED,
        PENDING
    }
}

