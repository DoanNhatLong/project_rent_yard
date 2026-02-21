package com.example.project_rent_yard.controller.client;

import com.example.project_rent_yard.dto.BookingDto;
import com.example.project_rent_yard.dto.ForeCastItem;
import com.example.project_rent_yard.dto.SearchDto;
import com.example.project_rent_yard.dto.TimeSlotDto;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

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
    @Autowired
    private ITimeSlotDtoService timeSlotDtoService;
    @Autowired
    private WeatherService weatherService;

    @ModelAttribute("fields")
    public Page<Field> getFieldList(Pageable pageable) {
        return fieldService.findAll(pageable);
    }

    @ModelAttribute
    public void addUserToSession(HttpSession session, Principal principal) {
        if (principal != null && session.getAttribute("user") == null) {
            User user = userService.findByName(principal.getName());
            session.setAttribute("user", user);
        }

//        User user = userService.findUserById(2);
//        session.setAttribute("user",user);
    }


    @GetMapping("")
    public String home(Model model) {
        List<ForeCastItem> forecast = weatherService.getDailyForecast();
        model.addAttribute("forecast", forecast);
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



    @PostMapping("/rent")
    public String rent(
            @ModelAttribute BookingDto bookingDto,
            @RequestParam Integer fieldId,
            HttpSession session,
            HttpServletRequest request
    ) {
        System.out.println("RENT CALLED");
        User user = (User) session.getAttribute("user");
        Field field = fieldService.findById(fieldId);
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setField(field);
        booking.setStartTime(bookingDto.getStartTime());
        booking.setBookingDate(bookingDto.getBookingDate());
        booking.setEndTime(bookingDto.getEndTime());
        bookingService.createBooking(booking);
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
        double fieldCost = booking.getField().getPrice() * hour;
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
    public String pay(HttpSession session) {
        Booking booking = (Booking) session.getAttribute("BOOKING");
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("SERVICE_MAP");
        Double total = (Double) session.getAttribute("total");
        String vnPayUrl = vnPayService.createVnPayUrl(booking.getId(), total);
        return "redirect:" + vnPayUrl;
    }

    @GetMapping("/vnpay-return")
    public String vnPayReturn(
            @RequestParam(required = false) Integer vnp_TxnRef,
            @RequestParam(required = false) Long vnp_Amount,
            RedirectAttributes redirectAttributes
    ) {
        double total = (double) vnp_Amount / 100;
        Booking booking = bookingService.findBookingById(vnp_TxnRef);
        booking.setDepositAmount(total);
        booking.setStatus(Booking.BookingStatus.COMPLETED);
        bookingService.updateBooking(booking);
        User user = booking.getUser();
        user.setTotalSpent(user.getTotalSpent() + total);
        userService.save(user);
        redirectAttributes.addFlashAttribute("mess", "Thuê sân thành công");
        return "redirect:/clients";
    }

    @PostMapping("/findField")
    @ResponseBody
    public List<Field> findField(
            @ModelAttribute SearchDto searchDto,
            HttpSession session
    ) {
        List<Integer> list = bookingService.searchBusyField(searchDto);
        System.out.println(list.size());
        List<Field> fieldList = fieldService.findAll();
        List<Field> fields = fieldList.stream()
                .filter(f -> searchDto.getFieldType() == null
                        || f.getFieldType() == searchDto.getFieldType())
                .filter(f -> !list.contains(f.getId()))
                .filter(f->f.getStatus()== Field.FieldStatus.AVAILABLE)
                .toList();
        session.setAttribute("DATA_SEARCH",searchDto);
        return fields;
    }

    @GetMapping("/detail/{id}")
    public String goDetail(@PathVariable Integer id, Model model) {
        Field field = fieldService.findById(id);
        LocalDate localDate = LocalDate.now();
        List<Booking> bookingList = bookingService.findBookingsByBookingDateAndFieldId(localDate, id);
        List<TimeSlotDto> slots = timeSlotDtoService.buildSlots(bookingList);
        System.out.println(bookingList.size());
        model.addAttribute("slots", slots);
        model.addAttribute("field", field);
        return "/client/detail";
    }

    @GetMapping("/booking")
    private String goBooking(@RequestParam ("fieldId") Integer fieldId){
        System.out.println("field");
        return "/client/booking";
    }

    @GetMapping("rent")
    public String goRent(@RequestParam("fieldId") Integer id, Model model) {
        Field field = fieldService.findById(id);
        model.addAttribute("field", field);
        List<Service> list = serviceService.findAll();
        model.addAttribute("service", list);
        return "/client/rent";
    }

    @GetMapping("/lease")
    public String goRent(
            @RequestParam("fieldId") Integer id,
            @RequestParam("selectedSlots") String slot,
            HttpSession session,
            Model model
    ) {
        Field field = fieldService.findById(id);
        model.addAttribute("field", field);
        LocalDate bookingDate = LocalDate.now();

        List<BookingDto> bookingDto = Arrays.stream(slot.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(timeStr -> {

                    LocalTime start = LocalTime.parse(timeStr);
                    LocalTime end = start.plusHours(1);

                    return new BookingDto(
                            bookingDate,
                            start,
                            end
                    );
                })
                .toList();
        System.out.println(bookingDto.size());
        session.setAttribute("bookingDto", bookingDto);
        List<Service> list = serviceService.findAll();
        model.addAttribute("service", list);
        return "/client/lease";
    }

    @GetMapping("/lease_checkout")
    public String goLeaseCheckout(
            @RequestParam ("fieldId") Integer fieldId,
            HttpSession session,
            Model model,
            HttpServletRequest request
    ) {
        List<BookingDto> bookingDto = (List<BookingDto>) session.getAttribute("bookingDto");
        if (bookingDto == null) {
            return "redirect:/clients";
        }
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
            } catch (NumberFormatException ignored) {}
        });

        session.setAttribute("SERVICE_MAP", serviceMap);
        model.addAttribute("bookingDto", bookingDto);
        Field field = fieldService.findById(fieldId);

        int totalHours = bookingDto.size();
        double fieldCost = field.getPrice() * totalHours;

        double serviceCost = 0;

        for (Map.Entry<Integer, Integer> entry : serviceMap.entrySet()) {
            Integer serviceId = entry.getKey();
            Integer quantity = entry.getValue();

            Service service = serviceService.findById(serviceId);
            serviceCost += service.getPrice() * quantity;
        }

        double totalCost = fieldCost + serviceCost;
        System.out.println(totalCost);
        model.addAttribute("totalCost",totalCost);
        User user= (User) session.getAttribute("user");
        Booking firstBooking=null;
        for (BookingDto dto : bookingDto) {
            Booking booking = new Booking();
            booking.setUser(user);
            booking.setField(field);
            booking.setStartTime(dto.getStartTime());
            booking.setEndTime(dto.getEndTime());
            booking.setBookingDate(dto.getBookingDate());
            bookingService.createBooking(booking);
            if (firstBooking==null){
                firstBooking=booking;
            }
        }
        String vnPayUrl = vnPayService.createVnPayUrl(firstBooking.getId(), totalCost);
        return "redirect:" + vnPayUrl;
    }



}
