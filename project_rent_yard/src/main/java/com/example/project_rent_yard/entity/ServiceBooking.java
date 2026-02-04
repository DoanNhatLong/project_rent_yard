package com.example.project_rent_yard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "service_booking", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"booking_id", "service_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    private boolean isDeleted=false;

    private int quantity;

}
