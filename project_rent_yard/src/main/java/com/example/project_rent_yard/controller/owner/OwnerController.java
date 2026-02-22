package com.example.project_rent_yard.controller.owner;

import com.example.project_rent_yard.entity.Field;
import com.example.project_rent_yard.service.IFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/owners")
public class OwnerController {
    @Autowired
    IFieldService fieldService;

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
            model.addAttribute("field", new Field());
        return "/owner/create";
    }
}
