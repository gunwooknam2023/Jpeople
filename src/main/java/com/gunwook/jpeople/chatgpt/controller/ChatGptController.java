package com.gunwook.jpeople.chatgpt.controller;

import com.gunwook.jpeople.chatgpt.service.ChatService;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class ChatGptController {

    private final ChatgptService chatgptService;
    private final ChatService chatService;

    /**
     * gpt에게 질문하기
     * @param question 질문
     * @return 응답
     */
    @PostMapping("/chat-gpt")
    ResponseEntity<String> chatGpt(@RequestBody String question){
        String result = chatService.chatGpt(question);
        return ResponseEntity.ok(result);
    }

}
