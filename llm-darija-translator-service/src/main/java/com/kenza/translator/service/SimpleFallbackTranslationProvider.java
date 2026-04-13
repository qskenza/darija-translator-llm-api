package com.kenza.translator.service;

import com.kenza.translator.model.TranslateResponse;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SimpleFallbackTranslationProvider implements TranslationProvider {
    private static final Map<String, String> DICTIONARY = new HashMap<>();

    static {
        DICTIONARY.put("hello", "salam");
        DICTIONARY.put("how are you", "labas 3lik");
        DICTIONARY.put("thank you", "shukran");
        DICTIONARY.put("good morning", "sba7 lkhir");
        DICTIONARY.put("good night", "tesba7 3la khir");
        DICTIONARY.put("what is your name", "smiytk شنو");
        DICTIONARY.put("i love morocco", "kanbghi lmaghrib");
        DICTIONARY.put("where are you going", "fin ghadi");
        DICTIONARY.put("see you later", "nchoufek mn be3d");
    }

    @Override
    public TranslateResponse translate(String text, String sourceLanguage, String targetLanguage) {
        String key = text == null ? "" : text.trim().toLowerCase(Locale.ROOT);
        String translated = DICTIONARY.getOrDefault(key,
                "[Fallback translation] " + text + " -> حاول تستعمل Gemini API key باش تاخذ ترجمة أفضل.");
        return new TranslateResponse(text, translated, sourceLanguage, targetLanguage, "fallback-local-dictionary");
    }
}
