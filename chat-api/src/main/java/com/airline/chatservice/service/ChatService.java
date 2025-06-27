package com.airline.chatservice.service;

import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final OpenAiService openAiService;

    public ChatService(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    public String processMessage(String message) {

        return openAiService.askChatbot(message);
    }
}

