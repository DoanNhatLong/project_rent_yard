package com.example.project_rent_yard.service;

import com.example.project_rent_yard.dto.*;
import com.example.project_rent_yard.entity.Booking;
import com.example.project_rent_yard.entity.ServiceBooking;
import com.example.project_rent_yard.exception.BookingConflictException;
import com.example.project_rent_yard.mapper.BookingMapper;
import com.example.project_rent_yard.repository.IBookingRepository;
import com.example.project_rent_yard.repository.IServiceRepository;
import com.example.project_rent_yard.specification.BookingSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService implements IBookingService {
    @Autowired
    IBookingRepository bookingRepository;
    @Autowired
    IServiceRepository serviceRepository;
    @Autowired
    ExtraService extraService;
    @Autowired
    IServiceBookingService serviceBookingService;


    @Override
    public void save(Booking booking) {
        bookingRepository.save(booking);
    }

    @Override
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking findBookingById(Integer id) {
        return bookingRepository.findBookingById(id);
    }

    @Override
    public List<Integer> searchBusyField(SearchDto searchDto) {
        List<Booking> bookings = bookingRepository.findAll(BookingSpecification.busyField(searchDto));
        return bookings.stream()
                .map(b -> b.getField().getId())
                .distinct()
                .toList()
                ;
    }

    @Override
    public List<Booking> findBookingsByBookingDateAndFieldId(LocalDate bookingDate, Integer field_id) {
        return bookingRepository.findBookingsByBookingDateAndFieldId(bookingDate, field_id);
    }

    @Override
    public List<Booking> findBookingsByUser_Id(Integer userId) {
        return bookingRepository.findBookingsByUser_Id(userId);
    }

    @Transactional
    @Override
    public void createBooking(Booking booking) {
        try {
            bookingRepository.save(booking);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            throw new BookingConflictException(
                    "Khung giờ này đã có người đặt."
            );
        }
    }

    @Transactional
    @Override
    public void updateBooking(Booking booking) {
        bookingRepository.save(booking);
    }

    @Override
    public Page<ViewBookingDto> getAll(Pageable pageable) {
        return bookingRepository.findAll(pageable)
                .map(BookingMapper::toViewBookingDto);
    }

    @Override
    public Page<ViewBookingDto> getAllForAdmin(Pageable pageable, Booking.BookingStatus status, String userName, LocalDate bookingDate) {
        return bookingRepository.findAllForAdmin(status, userName, bookingDate, pageable)
                .map(BookingMapper::toViewBookingDto);
    }

    @Override
    public Page<ViewBookingDto> getAllForAdmin(Pageable pageable, BookingFilter bookingFilter) {
        Specification<Booking> specification =
                BookingSpecification.filterBooking(bookingFilter);

        Page<Booking> bookings =
                bookingRepository.findAll(specification, pageable);

        return bookings.map(BookingMapper::toViewBookingDto);
    }

    @Transactional
    @Override
    public void saveServices(AddServiceForm form) {

        Booking booking = bookingRepository
                .findBookingById(form.getBookingId());

        // =========================
        // 1️⃣ Xử lý service có quantity
        // =========================
        if (form.getQuantities() != null) {

            form.getQuantities().forEach((serviceId, quantity) -> {

                if (quantity != null && quantity > 0) {

                    Optional<ServiceBooking> optional =
                            serviceBookingService
                                    .findByBookingIdAndServiceId(
                                            booking.getId(), serviceId);

                    if (optional.isPresent()) {

                        // Nếu đã tồn tại → cộng thêm quantity
                        ServiceBooking existing = optional.get();
                        existing.setQuantity(
                                existing.getQuantity() + quantity
                        );
                        serviceBookingService.save(existing);

                    } else {

                        // Nếu chưa tồn tại → tạo mới
                        com.example.project_rent_yard.entity.Service service =
                                serviceRepository.findById(serviceId)
                                        .orElseThrow(() ->
                                                new RuntimeException("Service không tồn tại"));

                        ServiceBooking sb = new ServiceBooking();
                        sb.setBooking(booking);
                        sb.setService(service);
                        sb.setQuantity(quantity);

                        serviceBookingService.save(sb);
                    }
                }
            });
        }

        // =========================
        // 2️⃣ Xử lý checkbox (quantity = 1)
        // =========================
        if (form.getSpecialServiceIds() != null) {

            for (Integer serviceId : form.getSpecialServiceIds()) {

                Optional<ServiceBooking> optional =
                        serviceBookingService
                                .findByBookingIdAndServiceId(
                                        booking.getId(), serviceId);

                if (optional.isPresent()) {

                    // Nếu đã tồn tại → tăng thêm 1
                    ServiceBooking existing = optional.get();
                    existing.setQuantity(existing.getQuantity() + 1);
                    serviceBookingService.save(existing);

                } else {

                    // Nếu chưa tồn tại → tạo mới với quantity = 1
                    com.example.project_rent_yard.entity.Service service =
                            serviceRepository.findById(serviceId)
                                    .orElseThrow(() ->
                                            new RuntimeException("Service không tồn tại"));

                    ServiceBooking sb = new ServiceBooking();
                    sb.setBooking(booking);
                    sb.setService(service);
                    sb.setQuantity(1);

                    serviceBookingService.save(sb);
                }
            }
        }
    }

    @Override
    public List<BookingHistoryDto> getBookingHistoryForUser(Integer userId) {
        return bookingRepository.getBookingHistoryForUser(userId);
    }


}
