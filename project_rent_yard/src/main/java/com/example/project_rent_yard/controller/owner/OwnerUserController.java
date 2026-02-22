package com.example.project_rent_yard.controller.owner;

import com.example.project_rent_yard.entity.Field;
import com.example.project_rent_yard.entity.User;
import com.example.project_rent_yard.service.IFieldService;
import com.example.project_rent_yard.service.IUserService;
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

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/owners/user")
public class OwnerUserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IFieldService fieldService;

    @ModelAttribute
    public void addUserToSession(HttpSession session, Principal principal) {
        if (principal != null && session.getAttribute("user") == null) {
            User user = userService.findByName(principal.getName());
            session.setAttribute("user", user);
        }
    }

    @GetMapping("")
    public String goUser(Model model) {
        List<User> users = userService.findAllByRole_NameOrRole_Name("user", "admin");
        model.addAttribute("users", users);
        model.addAttribute("newUser", new User());
        return "/owner/user";
    }

}
