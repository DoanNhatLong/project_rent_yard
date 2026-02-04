package com.example.project_rent_yard.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admins")
@Controller
public class AdminController {
    @GetMapping("")
    public String adminHome() {
        return "/admin/home";
    }

    @GetMapping("/chat")
    public String adminChat() {
        return "/admin/chat";
    }
}
