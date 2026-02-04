package com.example.project_rent_yard.controller;

import com.example.project_rent_yard.dto.RegisterDto;
import com.example.project_rent_yard.dto.RegisterValidator;
import com.example.project_rent_yard.entity.User;
import com.example.project_rent_yard.service.IUserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/login")
public class LogInController {
    @Autowired
    private IUserService userService;

    @Autowired
    private RegisterValidator registerValidator;

    @GetMapping("")
    public String login() {
        return "/login";
    }

    @PostMapping("")
    public String loginPost(
            @RequestParam String name,
            @RequestParam String password,
            HttpSession session
    ) {
        List<User> userList = userService.findAll();
        User temp = null;
        for (User user : userList) {
            if (Objects.equals(user.getName(), name) && Objects.equals(user.getPassword(), password)) {
                temp = user;
                break;
            }
        }
        if (temp != null) {
            session.setAttribute("user", temp);
            if (temp.getRole().getId() == 2) {
                return "redirect:/clients";
            } else {
                return "redirect:/admins";
            }
        } else {
            return "/login";
        }
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "/register";
    }

    @PostMapping("/register")
    public String check(
            @Valid @ModelAttribute("registerDto") RegisterDto registerDto,
            BindingResult bindingResult
    ){
        registerValidator.validate(registerDto,bindingResult);
        if (bindingResult.hasErrors()) {
            return "register";
        }
        return "redirect:/login";
    }
}
