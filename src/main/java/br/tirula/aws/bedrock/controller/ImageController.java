package br.tirula.aws.bedrock.controller;

import org.json.JSONObject;
import org.json.JSONPointer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

@RestController
public class ImageController {

    @Autowired
    private BedrockRuntimeClient bedrockRuntimeClient;
    private static String modelId = "amazon.titan-image-generator-v1";

    @PostMapping("/ai/image")
    public Object generate(@RequestBody String prompt) {
        var nativeRequestTemplate = """
                {
                    "taskType": "TEXT_IMAGE",
                    "textToImageParams": { "text": "{{prompt}}" },
                    "imageGenerationConfig": { "seed": {{seed}} }
                }""";
        // Get a random 31-bit seed for the image generation (max. 2,147,483,647).
        var seed = new BigInteger(31, new SecureRandom());
        var nativeRequest = nativeRequestTemplate
                .replace("{{prompt}}", prompt)
                .replace("{{seed}}", seed.toString());
        try {
            var response = bedrockRuntimeClient.invokeModel(request -> request
                    .body(SdkBytes.fromUtf8String(nativeRequest))
                    .modelId(modelId)
            );
            var responseBody = new JSONObject(response.body().asUtf8String());
            var base64ImageData = new JSONPointer("/images/0").queryFrom(responseBody).toString();
            return base64ImageData;

        } catch (Exception e) {
            return "Error" + e.getMessage();
        }
    }
}
