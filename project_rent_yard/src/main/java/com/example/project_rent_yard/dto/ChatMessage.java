package com.example.project_rent_yard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    private Integer senderId;
    private Integer receiverId;
    private String content;
    private String senderRole;

    public ChatMessage(String senderRole, String content) {
        this.senderRole = senderRole;
        this.content = content;
    }
}


