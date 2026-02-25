package com.example.project_rent_yard.controller.owner;

import com.example.project_rent_yard.dto.FieldCreateDto;
import com.example.project_rent_yard.entity.Field;
import com.example.project_rent_yard.repository.projection.ServiceBookingView;
import com.example.project_rent_yard.repository.projection.ServiceStatisticView;
import com.example.project_rent_yard.service.IFieldService;
import com.example.project_rent_yard.service.IServiceBookingService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/owners")
public class OwnerController {
    @Autowired
    IFieldService fieldService;
    @Autowired
    IServiceBookingService serviceBookingService;

    @GetMapping("")
    public String ownerHome() {
        return "/owner/home";
    }

    @GetMapping("/yard")
    public String goYard(
            Model model,
            @PageableDefault(size = 6) Pageable pageable
    ) {
        Page<Field> fieldPage = fieldService.findAll(pageable);
        model.addAttribute("fieldPage", fieldPage);
        model.addAttribute("fields", fieldPage.getContent());
        return "/owner/yard";
    }

    @GetMapping("/yard/price")
    public String goPrice(
            @RequestParam Integer fieldId,
            @RequestParam Double price) {
        Field field = fieldService.findById(fieldId);
        field.setPrice(price);
        fieldService.save(field);
        return "redirect:/owners/yard";
    }

    @GetMapping("/yard/status/{id}")
    public String goStatus(
            @PathVariable Integer id) {
        Field field = fieldService.findById(id);
        if (field.getStatus() == Field.FieldStatus.AVAILABLE) {
            field.setStatus(Field.FieldStatus.MAINTENANCE);
        } else {
            field.setStatus(Field.FieldStatus.AVAILABLE);
        }
        fieldService.save(field);
        return "redirect:/owners/yard";
    }

    @GetMapping("/addField")
    public String createField(Model model) {
        model.addAttribute("statuses", Field.FieldStatus.values());
        model.addAttribute("fieldTypes", Field.FieldType.values());
        model.addAttribute("field", new FieldCreateDto());
        return "/owner/create";
    }

    @PostMapping("/addField")
    public String saveField(
            @Valid @ModelAttribute("field") FieldCreateDto field,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", Field.FieldStatus.values());
            model.addAttribute("fieldTypes", Field.FieldType.values());
            return "/owner/create";
        }
        Field field1= new Field();
        BeanUtils.copyProperties(field,field1);
        fieldService.save(field1);
        return "redirect:/owners/yard";
    }

    @GetMapping("/profit")
    public String goProfit(Model model) {

        List<ServiceBookingView> data =
                serviceBookingService.getAllServiceBookingViews();

        long totalRevenue = 0;
        long totalFieldRevenue = 0;
        long totalServiceRevenue = 0;
        long totalBooking = data.size();

        Map<Integer, Long> hourCount = new HashMap<>();
        Map<LocalDate, Long> revenueByDate = new TreeMap<>();
        Map<String, Long> revenueByField = new HashMap<>();
        Map<Integer, Long> revenueByHour = new TreeMap<>();

        for (ServiceBookingView d : data) {

            int start = d.getStartTime().getHour();
            int end = d.getEndTime().getHour();
            int slot = end - start;

            long fieldMoney = (long) (slot * d.getFieldPrice());
            Double serviceMoney =  d.getTotalServiceAmount();
            long money = (long) (fieldMoney + serviceMoney);

            // ===== KPI =====
            totalFieldRevenue += fieldMoney;
            totalServiceRevenue += serviceMoney;
            totalRevenue += money;

            // ===== Peak time =====
            for (int h = start; h < end; h++) {
                hourCount.merge(h, 1L, Long::sum);
            }

            // ===== Reports =====
            revenueByDate.merge(d.getBookingDate(), money, Long::sum);
            revenueByField.merge(d.getFieldName(), money, Long::sum);
            revenueByHour.merge(start, money, Long::sum);
        }

        Integer peakTime = hourCount.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        model.addAttribute("data", data);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalFieldRevenue", totalFieldRevenue);
        model.addAttribute("totalServiceRevenue", totalServiceRevenue);
        model.addAttribute("totalBooking", totalBooking);
        model.addAttribute("peakTime", peakTime);

        model.addAttribute("revenueByDate", revenueByDate);
        model.addAttribute("revenueByField", revenueByField);
        model.addAttribute("revenueByHour", revenueByHour);

        // ===== SERVICE STATISTICS =====
        List<ServiceStatisticView> serviceStats =
                serviceBookingService.getServiceStatistics();

        Map<String, Long> serviceUsage = new LinkedHashMap<>();
        Map<String, Long> revenueByService = new LinkedHashMap<>();

        for (ServiceStatisticView s : serviceStats) {

            serviceUsage.put(
                    s.getServiceName(),
                    s.getTotalQuantity()
            );

            revenueByService.put(
                    s.getServiceName(),
                    s.getTotalRevenue().longValue()
            );
        }

        ServiceStatisticView topService =
                serviceStats.isEmpty() ? null : serviceStats.get(0);

        model.addAttribute("serviceUsage", serviceUsage);
        model.addAttribute("revenueByService", revenueByService);
        model.addAttribute("topService", topService);

        return "/owner/profit";
    }


}
