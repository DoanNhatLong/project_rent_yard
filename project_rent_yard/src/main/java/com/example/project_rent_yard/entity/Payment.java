package com.example.project_rent_yard.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    private double amount;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        PENDING, SUCCESS, FAILED
    }

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    public enum PaymentType {
        CASH, ONLINE
    }

    private boolean isDeleted=false;

    private LocalDateTime createdAt;
}
