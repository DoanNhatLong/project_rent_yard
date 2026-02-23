package com.example.project_rent_yard.controller.admin;

import com.example.project_rent_yard.dto.AddServiceForm;
import com.example.project_rent_yard.dto.BookingFilter;
import com.example.project_rent_yard.dto.ViewBookingDto;
import com.example.project_rent_yard.entity.Booking;
import com.example.project_rent_yard.entity.Service;
import com.example.project_rent_yard.service.IBookingService;
import com.example.project_rent_yard.service.IServiceService;
import com.example.project_rent_yard.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admins/booking")
public class BookingController {
    @Autowired
    IBookingService bookingService;
    @Autowired
    IUserService userService;
    @Autowired
    IServiceService serviceService;


    @GetMapping("")
    public String goBooking(
            Model model,
            BookingFilter bookingFilter,
            @PageableDefault(size = 10, sort = "bookingDate", direction = Sort.Direction.DESC)
            Pageable pageable
            ) {


        Page<ViewBookingDto> bookings=bookingService.getAllForAdmin(pageable, bookingFilter);
        model.addAttribute("filter", bookingFilter);
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

    @GetMapping("/addService/{id}")
    public String addService(
            @PathVariable("id") Integer bookingId,
            Model model){
        List<Service> serviceList=serviceService.findAll();
        model.addAttribute("bookingId", bookingId);
        model.addAttribute("services",serviceList);
        return "/admin/add-service";
    }

    @PostMapping("/add-service")
    public String saveService(
            @ModelAttribute AddServiceForm form
            ) {

        System.out.println("bookingId = " + form.getBookingId());
        System.out.println("specialServiceIds = " + form.getSpecialServiceIds());
        System.out.println("quantities = " + form.getQuantities());
        bookingService.saveServices(form);

        return "redirect:/admins/booking";
    }
}
