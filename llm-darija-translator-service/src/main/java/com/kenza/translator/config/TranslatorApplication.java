package com.kenza.translator.config;

import com.kenza.translator.auth.BasicAuthFilter;
import com.kenza.translator.resource.TranslatorResource;
import com.kenza.translator.util.CorsResponseFilter;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

public class TranslatorApplication extends ResourceConfig {
    public TranslatorApplication() {
        packages("com.kenza.translator");
        register(TranslatorResource.class);
        register(BasicAuthFilter.class);
        register(CorsResponseFilter.class);
        register(JacksonJsonProvider.class);
    }
}
