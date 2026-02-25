package com.example.project_rent_yard.repository;

import com.example.project_rent_yard.entity.ServiceBooking;
import com.example.project_rent_yard.repository.projection.ServiceBookingView;
import com.example.project_rent_yard.repository.projection.ServiceStatisticView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IServiceBookingRepository extends JpaRepository<ServiceBooking,Integer> {
    Optional<ServiceBooking> findByBookingIdAndServiceId(Integer bookingId, Integer serviceId);
    @Query(value = """
SELECT
    b.id AS bookingId,
    f.name AS fieldName,
    b.booking_date AS bookingDate,
    b.start_time AS startTime,
    b.end_time AS endTime,
    f.price AS fieldPrice,
    COALESCE(SUM(sb.quantity * s.price), 0) AS totalServiceAmount
FROM booking b
JOIN field f ON b.field_id = f.id
LEFT JOIN service_booking sb ON sb.booking_id = b.id
LEFT JOIN service s ON s.id = sb.service_id
WHERE MONTH(b.booking_date) = MONTH(CURRENT_DATE())
  AND YEAR(b.booking_date) = YEAR(CURRENT_DATE())
GROUP BY
    b.id,
    f.name,
    b.booking_date,
    b.start_time,
    b.end_time,
    f.price
ORDER BY b.booking_date DESC, b.start_time
""", nativeQuery = true)
    List<ServiceBookingView> getAllServiceBookingViews();
    @Query(value = """
SELECT
    s.name AS serviceName,
    SUM(sb.quantity) AS totalQuantity,
    SUM(sb.quantity * s.price) AS totalRevenue
FROM service_booking sb
JOIN service s ON s.id = sb.service_id
GROUP BY s.name
ORDER BY totalQuantity DESC
""", nativeQuery = true)
    List<ServiceStatisticView> getServiceStatistics();
}
