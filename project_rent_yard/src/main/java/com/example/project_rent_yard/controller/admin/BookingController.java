package com.example.project_rent_yard.controller.admin;

import com.example.project_rent_yard.dto.ViewBookingDto;
import com.example.project_rent_yard.entity.Booking;
import com.example.project_rent_yard.service.IBookingService;
import com.example.project_rent_yard.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admins/booking")
public class BookingController {
    @Autowired
    IBookingService bookingService;

    @Autowired
    IUserService userService;


    @GetMapping("")
    public String goBooking(
            Model model,
            @RequestParam(required = false) Booking.BookingStatus status,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) LocalDate bookingDate,
            @PageableDefault(size = 10, sort = "bookingDate", direction = Sort.Direction.DESC)
            Pageable pageable
            ) {

        Page<ViewBookingDto> bookings=bookingService.getAllForAdmin(pageable, status, userName, bookingDate);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("status", Booking.BookingStatus.values());
        model.addAttribute("page",bookings);
        model.addAttribute("data",bookings.getContent());
        model.addAttribute("headers", List.of(
                "Tên khách",
                "SĐT",
                "ID Sân",
                "Giờ bắt đầu",
                "Giờ kết thúc",
                "Trạng thái",
                "Ngày đặt"
        ));
        model.addAttribute("fields", List.of(
                "userName",
                "userPhone",
                "fieldId",
                "startTime",
                "endTime",
                "status",
                "bookingDate"
        ));
        return "/admin/booking";
    }
}
