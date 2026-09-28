package com.kenza.translator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenza.translator.model.TranslateResponse;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class GeminiTranslationProvider implements TranslationProvider {
    private final ObjectMapper mapper = new ObjectMapper();
    private final String apiKey;
    private final String model;

    public GeminiTranslationProvider(String apiKey, String model) {
        this.apiKey = apiKey;
        this.model = model;
    }

    @Override
    public TranslateResponse translate(String text, String sourceLanguage, String targetLanguage) {
        try {
            System.out.println("DEBUG apiKey present = " + (apiKey != null && !apiKey.isBlank()));
            System.out.println("DEBUG model = " + model);

            if (apiKey == null || apiKey.isBlank()) {
                throw new RuntimeException("Gemini API key is missing or empty");
            }

            String prompt = """
                    Translate the following text from %s to %s.
                    Return only the translated text.
                    Do not add explanations.
                    Do not add quotation marks.
                    Do not add notes.
                    Text: %s
                    """.formatted(sourceLanguage, targetLanguage, text);

            String url = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent";
            HttpURLConnection connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("x-goog-api-key", apiKey);
            connection.setDoOutput(true);
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);

            Map<String, Object> body = Map.of(
                    "contents", new Object[]{
                            Map.of("parts", new Object[]{
                                    Map.of("text", prompt)
                            })
                    }
            );

            try (OutputStream os = connection.getOutputStream()) {
                os.write(mapper.writeValueAsBytes(body));
            }

            int status = connection.getResponseCode();
            InputStream stream = status >= 200 && status < 300
                    ? connection.getInputStream()
                    : connection.getErrorStream();

            if (stream == null) {
                throw new RuntimeException("Gemini API returned no response body. HTTP status: " + status);
            }

            String responseBody = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println("DEBUG Gemini HTTP status = " + status);
            System.out.println("DEBUG Gemini raw response = " + responseBody);

            JsonNode root = mapper.readTree(responseBody);

            if (status < 200 || status >= 300) {
                throw new RuntimeException("Gemini API error: " + root.toPrettyString());
            }

            JsonNode textNode = root.path("candidates").path(0).path("content").path(0).path("parts").path(0).path("text");
            if (textNode.isMissingNode()) {
                textNode = root.path("candidates").path(0).path("content").path("parts").path(0).path("text");
            }

            String translated = textNode.asText("").trim();

            if (translated.isEmpty()) {
                throw new RuntimeException("Gemini response did not contain translated text");
            }

            return new TranslateResponse(text, translated, sourceLanguage, targetLanguage, model);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to call Gemini API", e);
        }
    }
}