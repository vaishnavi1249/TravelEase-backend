package com.example.travelease;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Value("${gemini.api.key:}")
    private String geminiApiKey;

    @PostMapping("/chat")
    public ResponseEntity<?> chatWithAi(@RequestBody Map<String, String> request) {
        String query = request.getOrDefault("query", "");

        if (geminiApiKey == null || geminiApiKey.isEmpty()) {
            return ResponseEntity.ok(Map.of("message",
                    "Simulated AI: I am a travel assistant. To use real AI, configure GEMINI_API_KEY. You asked: "
                            + query));
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro-latest:generateContent?key="
                    + geminiApiKey;

            String promptText = "You are a helpful travel assistant for a platform called TravelEase. Keep your answer brief, warm, and highly relevant. The user says: "
                    + query;

            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(
                                    Map.of("text", promptText)))));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            Map<String, Object> body = response.getBody();

            if (body != null && body.containsKey("candidates")) {
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) body.get("candidates");
                if (!candidates.isEmpty()) {
                    Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                    List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                    if (!parts.isEmpty()) {
                        String generatedText = (String) parts.get(0).get("text");
                        return ResponseEntity.ok(Map.of("message", generatedText));
                    }
                }
            }
            return ResponseEntity
                    .ok(Map.of("message", "I couldn't generate a proper response at this time. Please try again."));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(
                    Map.of("message", "Simulated AI: An error occurred calling the real AI API. " + e.getMessage()));
        }
    }
}
