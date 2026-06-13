package com.mediscan.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediscan.dto.PrescriptionResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PrescriptionService {

    @Autowired
    private WebClient webClient;

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public PrescriptionResponseDTO scanPrescription(MultipartFile file) throws Exception {

        // STEP 1: Image → Base64
        byte[] imageBytes = file.getBytes();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        String mimeType = file.getContentType() != null ? file.getContentType() : "image/jpeg";
        System.out.println("STEP 1: Image converted ✅ | " + file.getOriginalFilename());

        // STEP 2: Build Groq request
        String prompt = """
                You are a medical prescription reader.
                Analyze this prescription image carefully.
                Return ONLY valid JSON. No explanation. No markdown. Just raw JSON:
                
                {
                  "doctor": "doctor name if visible, else null",
                  "patient": "patient name if visible, else null",
                  "date": "date if visible, else null",
                  "medicines": [
                    {
                      "name": "medicine name",
                      "dosage": "like 500mg, else null",
                      "frequency": "like twice daily, else null",
                      "duration": "like 5 days, else null",
                      "instructions": "like after food, else null"
                    }
                  ],
                  "notes": "any extra notes, else null"
                }
                """;

        // Image content
        Map<String, Object> imageUrl = new HashMap<>();
        imageUrl.put("url", "data:" + mimeType + ";base64," + base64Image);

        Map<String, Object> imageContent = new HashMap<>();
        imageContent.put("type", "image_url");
        imageContent.put("image_url", imageUrl);

        // Text content
        Map<String, Object> textContent = new HashMap<>();
        textContent.put("type", "text");
        textContent.put("text", prompt);

        // Message
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", List.of(textContent, imageContent));

        // Request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "meta-llama/llama-4-scout-17b-16e-instruct");
        requestBody.put("max_tokens", 1000);
        requestBody.put("messages", List.of(message));

        System.out.println("STEP 2: Request body ready ✅");

        // STEP 3: Call Groq API
        System.out.println("STEP 3: Calling Groq API...");

        String rawResponse = webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("STEP 3: Response received ✅");
        System.out.println("Raw: " + rawResponse);

        // STEP 4: Parse response
        // Groq response same as OpenAI:
        // { "choices": [ { "message": { "content": "..." } } ] }
        JsonNode responseNode = objectMapper.readTree(rawResponse);

        if (responseNode.has("error")) {
            String errorMsg = responseNode.path("error").path("message").asText();
            throw new RuntimeException("Groq API Error: " + errorMsg);
        }

        String aiText = responseNode
                .path("choices").get(0)
                .path("message")
                .path("content")
                .asText()
                .trim();

        System.out.println("STEP 4: AI Text: " + aiText);

        // STEP 5: Clean and parse JSON
        String cleanJson = aiText
                .replaceAll("(?s)```json", "")
                .replaceAll("(?s)```", "")
                .trim();

        int start = cleanJson.indexOf('{');
        int end = cleanJson.lastIndexOf('}');

        if (start == -1 || end == -1) {
            throw new RuntimeException("No JSON found: " + aiText);
        }

        cleanJson = cleanJson.substring(start, end + 1);

        PrescriptionResponseDTO result = objectMapper.readValue(
                cleanJson, PrescriptionResponseDTO.class);

        System.out.println("STEP 5: Done! Medicines: " +
                (result.getMedicines() != null ? result.getMedicines().size() : 0));

        return result;
    }
}