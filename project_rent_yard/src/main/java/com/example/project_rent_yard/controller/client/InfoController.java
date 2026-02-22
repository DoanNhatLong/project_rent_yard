package com.example.project_rent_yard.controller.client;

import com.example.project_rent_yard.entity.Booking;
import com.example.project_rent_yard.entity.User;
import com.example.project_rent_yard.service.IBookingService;
import com.example.project_rent_yard.service.IFieldService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/userInfo")
public class InfoController {
    @Autowired
    IBookingService bookingService;
    @Autowired
    IFieldService fieldService;

    @GetMapping("")
    public String goInfo(
            Model model,
            HttpSession session
    ){
        User user= (User) session.getAttribute("user");
        List<Booking> bookingList=bookingService.findBookingsByUser_Id(user.getId());
        model.addAttribute("bookings", bookingList);
        return "/client/info";
    }

    @GetMapping("/review/{fieldId}")
    public String goReview(
            @PathVariable Integer fieldId,
            Model model
    ){
        model.addAttribute("field",fieldService.findById(fieldId));
        return "client/review";
    }
}
