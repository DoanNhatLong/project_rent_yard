package com.example.project_rent_yard.controller.admin;

import com.example.project_rent_yard.dto.BookingRequest;
import com.example.project_rent_yard.dto.TimeSlotDto;
import com.example.project_rent_yard.entity.Booking;
import com.example.project_rent_yard.entity.Field;
import com.example.project_rent_yard.entity.User;
import com.example.project_rent_yard.service.IBookingService;
import com.example.project_rent_yard.service.IFieldService;
import com.example.project_rent_yard.service.ITimeSlotDtoService;
import com.example.project_rent_yard.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/admins")
@Controller
public class AdminController {
    @Autowired
    IUserService userService;
    @Autowired
    IFieldService fieldService;
    @Autowired
    IBookingService bookingService;
    @Autowired
    ITimeSlotDtoService timeSlotDtoService;

    @ModelAttribute
    public void addUserToSession(HttpSession session, Principal principal) {
        if (principal != null && session.getAttribute("user") == null) {
            User user = userService.findByName(principal.getName());
            session.setAttribute("user", user);
        }
    }

    @GetMapping("")
    public String adminHome() {
        return "/admin/home";
    }

    @GetMapping("/chat")
    public String adminChat() {
        return "/admin/chat";
    }

    @GetMapping("user")
    public String goUser(Model model){
        List<User> users=userService.findAllByRole_Name("user");
        model.addAttribute("users",users);
        return "/admin/user";
    }

    @GetMapping("yard")
    public String goYard(
            @RequestParam(required = false) Field.FieldType fieldType,
            @RequestParam(required = false) Field.FieldStatus fieldStatus,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @PageableDefault(size = 4)Pageable pageable,
            Model model
            ){
        Page<Field> fieldPage=fieldService.showFields(fieldType,fieldStatus,minPrice,maxPrice,pageable);
        Map<Integer, List<TimeSlotDto>> slotMap = new HashMap<>();
        for (Field field : fieldPage.getContent()) {
            LocalDate localDate = LocalDate.now();
            List<Booking> bookingList = bookingService.findBookingsByBookingDateAndFieldId(localDate, field.getId());
            List<TimeSlotDto> slots = timeSlotDtoService.buildSlots(bookingList);
            slotMap.put(field.getId(), slots);
        }
        model.addAttribute("fieldPage", fieldPage);
        model.addAttribute("fields", fieldPage.getContent());
        model.addAttribute("slotMap", slotMap);
        return "/admin/yard";
    }

    @GetMapping("/look/{fieldID}")
    public String rent(@PathVariable Integer fieldID){
        LocalDate localDate = LocalDate.now();
        List<Booking> bookingList = bookingService.findBookingsByBookingDateAndFieldId(localDate, fieldID);
        List<TimeSlotDto> slots = timeSlotDtoService.buildSlots(bookingList);
        System.out.println(slots.size());
        return "/client/home";
    }

    @PostMapping("/api/book")
    @ResponseBody
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest, HttpSession session){
        Booking booking = new Booking();
        Field field = fieldService.findById(bookingRequest.getFieldId());
        booking.setField(field);
        LocalTime startTime = LocalTime.parse(
                bookingRequest.getStartTime().substring(0, 5)
        );
        User user= (User) session.getAttribute("user");
        System.out.println(user.getId());
        booking.setUser(user);
        booking.setStartTime(startTime);
        booking.setEndTime(startTime.plusHours(1));
        booking.setBookingDate(LocalDate.now());
        booking.setStatus(Booking.BookingStatus.COMPLETED);
        booking.setDepositAmount(field.getPrice());
        bookingService.save(booking);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Đặt sân thành công"
        ));
    }
}
