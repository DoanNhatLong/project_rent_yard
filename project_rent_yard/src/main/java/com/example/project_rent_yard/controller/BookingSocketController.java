package com.example.project_rent_yard.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class BookingSocketController {

    @MessageMapping("/booking")
    @SendTo("/topic/booking")
    public String handleBooking(String message) {
        return message;
    }
}
