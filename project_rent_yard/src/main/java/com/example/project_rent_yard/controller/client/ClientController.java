package com.example.project_rent_yard.controller.client;

import com.example.project_rent_yard.entity.Field;
import com.example.project_rent_yard.entity.User;
import com.example.project_rent_yard.service.IFieldService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/clients")
@Controller
public class ClientController {
    @Autowired
    private IFieldService fieldService;

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

}
