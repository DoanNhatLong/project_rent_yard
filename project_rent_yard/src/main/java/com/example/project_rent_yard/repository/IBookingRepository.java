package com.example.project_rent_yard.repository;

import com.example.project_rent_yard.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookingRepository extends JpaRepository<Booking,Integer> {
}
