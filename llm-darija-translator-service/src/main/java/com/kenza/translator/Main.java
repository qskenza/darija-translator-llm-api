package com.kenza.translator;

import com.kenza.translator.config.TranslatorApplication;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import java.io.IOException;
import java.net.URI;

public class Main {
    public static void main(String[] args) throws IOException {
        String baseUrl = System.getenv().getOrDefault("BASE_URL", "http://0.0.0.0:8080/");
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUrl), new TranslatorApplication());
        System.out.println("Darija translator running at " + baseUrl + "api/");
        System.out.println("Public health endpoint: " + baseUrl + "api/translator/health");
        System.out.println("Protected translate endpoint: " + baseUrl + "api/translator/translate");
        System.out.println("Press Enter to stop...");
        System.in.read();
        server.shutdownNow();
    }
}
