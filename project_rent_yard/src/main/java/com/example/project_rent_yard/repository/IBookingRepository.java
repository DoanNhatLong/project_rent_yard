package com.example.project_rent_yard.repository;

import com.example.project_rent_yard.dto.ViewBookingDto;
import com.example.project_rent_yard.entity.Booking;
import com.example.project_rent_yard.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface IBookingRepository extends JpaRepository<Booking,Integer>, JpaSpecificationExecutor<Booking> {
    Booking findBookingById(Integer id);
    List<Booking> findBookingsByBookingDateAndFieldId(LocalDate bookingDate, Integer field_id);
    List<Booking> findBookingsByUser_Id(Integer userId);

    @Modifying
    @Transactional
    @Query("""
        UPDATE Booking b
        SET b.status = 'CANCELLED_BY_OWNER'
        WHERE b.status = 'PENDING'
        AND b.expiredAt <= :now
    """)
    void cancelExpired(@Param("now") LocalDateTime now);

}
