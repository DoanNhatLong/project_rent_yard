package com.example.project_rent_yard.repository;

import com.example.project_rent_yard.entity.ServiceBooking;
import com.example.project_rent_yard.repository.projection.ServiceBookingView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IServiceBookingRepository extends JpaRepository<ServiceBooking,Integer> {
    Optional<ServiceBooking> findByBookingIdAndServiceId(Integer bookingId, Integer serviceId);
    @Query(value = """
select sb.id, sb.quantity, f.name as fieldName, f.price as fieldPrice,s.name as serviceName, s.price as servicePrice
from service_booking sb
join booking b on sb.booking_id=b.id
join field f on b.field_id=f.id
join service s on s.id=sb.service_id
order by sb.id;
""", nativeQuery = true)
    List<ServiceBookingView> getAllServiceBookingViews();
}
