package com.example.project_rent_yard.specification;

import com.example.project_rent_yard.dto.SearchDto;
import com.example.project_rent_yard.entity.Booking;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BookingSpecification {
    public static Specification<Booking> busyField(SearchDto searchDto) {
        return ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(
                    cb.equal(root.get("bookingDate"), searchDto.getBookingDate())
            );

            if (searchDto.getStartTime() != null) {
                predicates.add(cb.greaterThan(root.get("endTime"), searchDto.getEndTime()));
            }

            if (searchDto.getEndTime() != null) {
                predicates.add(cb.lessThan(root.get("startTime"), searchDto.getStartTime()));
            }

            query.distinct(true);

            return cb.and(predicates.toArray(new Predicate[0]));
        });

    }
}
