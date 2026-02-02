package com.example.project_rent_yard.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "booking")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private BookingStatus status;

    private double depositAmount;

    private boolean isDeleted=false;

    private LocalDateTime createdAt;

    public enum BookingStatus {
        BOOKED,
        CANCELLED_BY_CUSTOMER,
        CANCELLED_BY_OWNER,
        COMPLETED
    }
}

