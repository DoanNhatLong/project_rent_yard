package com.example.project_rent_yard.repository;


import com.example.project_rent_yard.dto.BookingHistoryDto;
import com.example.project_rent_yard.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Query("""
select b from Booking b
where (:status is null or b.status = :status)
and (
      :userName is null
      or :userName = ''
      or lower(b.user.name) like lower(concat('%', :userName, '%'))
)
and (:bookingDate is null or b.bookingDate = :bookingDate)
""")
    Page<Booking> findAllForAdmin(
            @Param("status") Booking.BookingStatus status,
            @Param("userName") String userName,
            @Param("bookingDate") LocalDate bookingDate,
            Pageable pageable
    );

    @Query("""
select new com.example.project_rent_yard.dto.BookingHistoryDto(
b.id,
b.user.id,
f.name,
f.price,
b.bookingDate,
b.startTime,
b.endTime,
b.status
)
from Booking b
join b.field f
where b.user.id = :userId
order by b.bookingDate desc, b.startTime desc
""")
    List<BookingHistoryDto> getBookingHistoryForUser(@Param("userId") Integer userId);
}
