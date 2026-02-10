package com.example.project_rent_yard.controller.client;

import com.example.project_rent_yard.dto.BookingDto;
import com.example.project_rent_yard.dto.SearchDto;
import com.example.project_rent_yard.entity.Booking;
import com.example.project_rent_yard.entity.Field;
import com.example.project_rent_yard.entity.Service;
import com.example.project_rent_yard.entity.User;
import com.example.project_rent_yard.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/clients")
@Controller
public class ClientController {
    @Autowired
    private IFieldService fieldService;
    @Autowired
    private IServiceService serviceService;
    @Autowired
    private IBookingService bookingService;
    @Autowired
    private IVnPayService vnPayService;
    @Autowired
    private IUserService userService;

    @ModelAttribute("fields")
    public Page<Field> getFieldList(Pageable pageable) {
        return fieldService.findAll(pageable);
    }

    @GetMapping("")
    public String home() {
        return "/client/home";
    }

    @GetMapping("/yards")
    public String yards(
            @RequestParam(required = false) Field.FieldType fieldType,
            @RequestParam(required = false) Field.FieldStatus fieldStatus,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @PageableDefault(size = 4) Pageable pageable,
            Model model
    ) {
        Page<Field> fieldPage = fieldService.showFields(fieldType, fieldStatus, minPrice, maxPrice, pageable);
        model.addAttribute("fieldPage", fieldPage);
        model.addAttribute("fields", fieldPage.getContent());
        return "/client/yards";
    }

    @GetMapping("/support")
    public String support(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "/client/support";
    }

    @GetMapping("/rent/{id}")
    public String goRent(@PathVariable Integer id, Model model) {
        Field field = fieldService.findById(id);
        model.addAttribute("field", field);
        List<Service> list = serviceService.findAll();
        model.addAttribute("service", list);
        return "/client/rent";
    }

    @PostMapping("/rent")
    public String rent(
            @ModelAttribute BookingDto bookingDto,
            @RequestParam Integer fieldId,
            HttpSession session,
            HttpServletRequest request
    ) {
        User user = (User) session.getAttribute("user");
        Field field = fieldService.findById(fieldId);
        System.out.println(bookingDto);
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setField(field);
        booking.setStartTime(bookingDto.getStartTime());
        booking.setBookingDate(bookingDto.getBookingDate());
        booking.setEndTime(bookingDto.getEndTime());
        bookingService.save(booking);
        Map<Integer, Integer> serviceMap = new HashMap<>();
        request.getParameterMap().forEach((key, values) -> {
            try {
                Integer serviceId = Integer.parseInt(key);
                String value = values[0];
                if ("true".equals(value)) {
                    serviceMap.put(serviceId, 1);
                } else {
                    int quantity = Integer.parseInt(value);
                    if (quantity > 0) {
                        serviceMap.put(serviceId, quantity);
                    }
                }
            } catch (NumberFormatException ignored) {
            }
        });
        session.setAttribute("BOOKING", booking);
        session.setAttribute("SERVICE_MAP", serviceMap);
        return "redirect:/clients/checkout";
    }

    @GetMapping("/checkout")
    public String goCheckOut(
            HttpSession session,
            Model model
    ) {
        Booking booking = (Booking) session.getAttribute("BOOKING");
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("SERVICE_MAP");
        if (cart == null) cart = Map.of();
        if (booking == null) {
            return "redirect:/clients";
        }
        int hour = booking.getEndTime().getHour() - booking.getStartTime().getHour();
        double fieldCost = booking.getField().getPrice()*hour;
        List<Service> serviceList = serviceService.findAllById(cart.keySet());
        double serviceCost = 0;
        for (Service s : serviceList) {
            serviceCost += s.getPrice() * cart.get(s.getId());
        }

        double total = fieldCost + serviceCost;

        model.addAttribute("booking", booking);
        model.addAttribute("hour", hour);
        model.addAttribute("services", serviceList);
        model.addAttribute("cart", cart);
        model.addAttribute("fieldCost", fieldCost);
        model.addAttribute("serviceCost", serviceCost);
        session.setAttribute("total", total);

        return "/client/checkout";
    }

    @GetMapping("/checkout/pay")
    public String pay(HttpSession session){
        Booking booking = (Booking) session.getAttribute("BOOKING");
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("SERVICE_MAP");
        Double total = (Double) session.getAttribute("total");
        String vnPayUrl=vnPayService.createVnPayUrl(booking.getId(), total);
        return "redirect:" + vnPayUrl;
    }

    @GetMapping("/vnpay-return")
    public String vnPayReturn(
            @RequestParam(required = false) Integer vnp_TxnRef,
            @RequestParam(required = false) Long vnp_Amount
    ) {
        double total= (double) vnp_Amount /100;
        Booking booking= bookingService.findBookingById(vnp_TxnRef);
        booking.setStatus(Booking.BookingStatus.COMPLETED);
        booking.setDepositAmount(total);
        bookingService.save(booking);
        User user=booking.getUser();
        user.setTotalSpent(user.getTotalSpent()+total);
        userService.save(user);
        return "redirect:/clients";
    }

    @PostMapping("/findField")
    public String findField(
            @ModelAttribute SearchDto searchDto
            ){
        List<Integer> list= bookingService.searchBusyField(searchDto);
        System.out.println(list);
        return "/client/find";
    }

    @GetMapping("/detail/{id}")
    public String goDetail(@PathVariable Integer id, Model model){
        Field field=fieldService.findById( id);
        model.addAttribute("field",field);
        return "/client/detail";
    }

}
