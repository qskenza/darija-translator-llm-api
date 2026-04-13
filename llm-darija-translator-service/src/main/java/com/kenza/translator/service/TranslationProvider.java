package com.kenza.translator.service;

import com.kenza.translator.model.TranslateResponse;

public interface TranslationProvider {
    TranslateResponse translate(String text, String sourceLanguage, String targetLanguage);
}
