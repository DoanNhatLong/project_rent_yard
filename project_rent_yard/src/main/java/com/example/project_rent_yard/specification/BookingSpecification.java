package com.example.project_rent_yard.specification;

import com.example.project_rent_yard.dto.SearchDto;
import com.example.project_rent_yard.entity.Booking;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BookingSpecification {

    public static Specification<Booking> busyField(SearchDto searchDto) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(
                    cb.equal(root.get("bookingDate"), searchDto.getBookingDate())
            );

            if (searchDto.getStartTime() != null && searchDto.getEndTime() != null) {
                predicates.add(
                        cb.lessThan(root.get("startTime"), searchDto.getEndTime())
                );
                predicates.add(
                        cb.greaterThan(root.get("endTime"), searchDto.getStartTime())
                );
            }

            if (searchDto.getFieldType() != null) {
                predicates.add(
                        cb.equal(root.get("field").get("fieldType"), searchDto.getFieldType())
                );
            }
            query.distinct(true);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
