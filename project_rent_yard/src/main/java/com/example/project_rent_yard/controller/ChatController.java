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

        // 1️⃣ Gửi tin nhắn cho người nhận (USER <-> ADMIN)
        simpMessagingTemplate.convertAndSend(
                "/topic/chat.user." + message.getReceiverId(),
                message
        );

        // 2️⃣ Gửi xác nhận về cho người gửi (GIỮ NGUYÊN CHỨC NĂNG CŨ)
        simpMessagingTemplate.convertAndSend(
                "/topic/chat.user." + message.getSenderId(),
                new ChatMessage("SERVER", "Đã nhận tin nhắn")
        );

        System.out.println(
                message.getSenderRole() + " [" +
                        message.getSenderId() + " -> " +
                        message.getReceiverId() + "] : " +
                        message.getContent()
        );
    }
}

