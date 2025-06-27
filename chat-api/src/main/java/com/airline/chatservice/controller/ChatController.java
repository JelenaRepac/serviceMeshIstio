package com.airline.chatservice.controller;

import com.airline.chatservice.model.ChatMessage;
import com.airline.chatservice.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/message")
    public ResponseEntity<ChatMessage> handleMessage(@RequestBody ChatMessage chatMessage) {
        String response = chatService.processMessage(chatMessage.getMessage());
        return ResponseEntity.ok(ChatMessage.builder().message(response).build());
    }
}

