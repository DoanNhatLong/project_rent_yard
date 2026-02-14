package com.example.project_rent_yard.service;

import com.example.project_rent_yard.dto.SearchDto;
import com.example.project_rent_yard.entity.Booking;
import com.example.project_rent_yard.exception.BookingConflictException;
import com.example.project_rent_yard.repository.IBookingRepository;
import com.example.project_rent_yard.specification.BookingSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService implements IBookingService {
    @Autowired
    IBookingRepository bookingRepository;
    @Autowired
    ExtraService extraService;


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
        List<Booking> bookings=bookingRepository.findAll(BookingSpecification.busyField(searchDto));
        return bookings.stream()
                .map(b->b.getField().getId())
                .distinct()
                .toList()
                ;
    }

    @Override
    public List<Booking> findBookingsByBookingDateAndFieldId(LocalDate bookingDate, Integer field_id) {
        return bookingRepository.findBookingsByBookingDateAndFieldId(bookingDate,field_id);
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

    @Transactional
    public void markCompleted(Integer bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow();

        booking.setStatus(Booking.BookingStatus.COMPLETED);

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        extraService.createSomething(bookingId);
                    }
                }
        );
    }

}
