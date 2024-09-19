package br.tirula.aws.bedrock.controller;

import org.springframework.ai.bedrock.titan.BedrockTitanChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ChatController {

    private final BedrockTitanChatModel chatModel;

    @Autowired
    public ChatController(BedrockTitanChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @PostMapping("/ai/chat")
    public Object generate(@RequestBody  String message) {
        return Map.of("generation", chatModel.call(message));
    }

}