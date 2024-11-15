package com.powerpuff.billServer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class GeminiChatService {

    private final WebClient webClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    public GeminiChatService(@Value("${gemini.api.url}") String geminiApiUrl,
                             @Value("${gemini.api.key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl(geminiApiUrl)
                .build();
    }

    public Mono<String> getAiResponse(String userMessage) {
        Map<String, Object> requestPayload = createRequestPayload(userMessage);

        return webClient.post()
                .uri("/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestPayload)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    // Handle exceptions from the API
                    System.err.println("Error response from Gemini API: " + ex.getMessage());
                    return Mono.just("Error processing request.");
                });
    }

    private Map<String, Object> createRequestPayload(String userMessage) {
        // Prepare the JSON payload structure as required by the API
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> content = new HashMap<>();
        content.put("text", userMessage);

        payload.put("contents", Arrays.asList(
                new HashMap<String, Object>() {{
                    put("parts", Arrays.asList(content));
                }}
        ));

        return payload;
    }
}
