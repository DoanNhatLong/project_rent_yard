package com.example.project_rent_yard.controller.admin;

import com.example.project_rent_yard.dto.ViewBookingDto;
import com.example.project_rent_yard.service.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admins/booking")
public class BookingController {
    @Autowired
    IBookingService bookingService;

    @GetMapping("")
    public String goBooking(Model model) {
        List<ViewBookingDto> bookings=bookingService.getAll();
        model.addAttribute("data",bookings);
        model.addAttribute("headers", List.of(
                "ID",
                "Tên khách",
                "SĐT",
                "Sân",
                "Giờ bắt đầu",
                "Giờ kết thúc",
                "Trạng thái",
                "Tiền cọc",
                "Ngày đặt"
        ));

        model.addAttribute("fields", List.of(
                "id",
                "userName",
                "userPhone",
                "fieldId",
                "startTime",
                "endTime",
                "status",
                "depositAmount",
                "bookingDate"
        ));
        return "/admin/booking";
    }
}
