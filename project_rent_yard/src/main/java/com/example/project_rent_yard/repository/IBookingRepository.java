package com.example.project_rent_yard.repository;

import com.example.project_rent_yard.entity.Booking;
import com.example.project_rent_yard.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IBookingRepository extends JpaRepository<Booking,Integer>, JpaSpecificationExecutor<Booking> {
    Booking findBookingById(Integer id);
    List<Booking> findBookingsByBookingDateAndFieldId(LocalDate bookingDate, Integer field_id);

    List<Booking> findBookingsByUser_Id(Integer userId);

}
