package com.example.project_rent_yard.controller;

import com.example.project_rent_yard.dto.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat.send")
    public void send(ChatMessage message) {

        simpMessagingTemplate.convertAndSend(
                "/topic/chat.user." + message.getReceiverId(),
                message
        );


        simpMessagingTemplate.convertAndSend(
                "/topic/chat.user." + message.getSenderId(),
                new ChatMessage("SERVER", "Đã nhận tin nhắn")
        );
    }
}

