package br.tirula.aws.bedrock.controller;


import org.springframework.ai.bedrock.titan.BedrockTitanEmbeddingOptions;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class EmbeddingController {

    private final EmbeddingModel embeddingModel;

    @Autowired
    public EmbeddingController(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @PostMapping("/ai/embedding")
    public Object embed(@RequestBody List<String> input) {
        EmbeddingResponse embeddingResponse = embeddingModel.call(
                new EmbeddingRequest(input,
                        BedrockTitanEmbeddingOptions.builder()
                                .build()));
        return Map.of("embedding", embeddingResponse);
    }
}