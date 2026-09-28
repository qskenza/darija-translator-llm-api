package com.kenza.translator.service;

import com.kenza.translator.model.TranslateResponse;

public class TranslationService {
    private final TranslationProvider fallbackProvider = new SimpleFallbackTranslationProvider();

    public TranslateResponse translate(String text, String sourceLanguage, String targetLanguage) {
        String apiKey = System.getenv("GEMINI_API_KEY");
        String model = System.getenv().getOrDefault("GEMINI_MODEL", "gemini-3.5-flash-lite");

        if (apiKey != null && !apiKey.isBlank()) {
            try {
                return new GeminiTranslationProvider(apiKey, model)
                        .translate(text, sourceLanguage, targetLanguage);
            } catch (Exception e) {
                return fallbackProvider.translate(text, sourceLanguage, targetLanguage);
            }
        }

        return fallbackProvider.translate(text, sourceLanguage, targetLanguage);
    }
}
