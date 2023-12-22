package com.gunwook.jpeople.chatgpt.service;

import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatgptService chatgptService;
    public String chatGpt(String question) {
        return chatgptService.sendMessage(question);
    }
}
