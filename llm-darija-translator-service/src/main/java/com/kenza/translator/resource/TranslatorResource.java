package com.kenza.translator.resource;

import com.kenza.translator.model.ErrorResponse;
import com.kenza.translator.model.TranslateRequest;
import com.kenza.translator.model.TranslateResponse;
import com.kenza.translator.service.TranslationService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/translator")
@Produces(MediaType.APPLICATION_JSON)
public class TranslatorResource {
    private final TranslationService translationService = new TranslationService();

    @GET
    @Path("/health")
    public Response health() {
        return Response.ok(Map.of(
                "status", "UP",
                "service", "LLM-powered Darija Translator",
                "auth", "Basic Authentication on protected endpoints"
        )).build();
    }

    @GET
    @Path("/info")
    public Response info() {
        return Response.ok(Map.of(
                "name", "TranslatorResource",
                "method", "translate",
                "defaultTargetLanguage", "Moroccan Darija"
        )).build();
    }

    @POST
    @Path("/translate")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response translate(TranslateRequest request) {
        if (request == null || request.getText() == null || request.getText().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Field 'text' is required."))
                    .build();
        }

        TranslateResponse response = translationService.translate(
                request.getText(),
                request.getSourceLanguage(),
                request.getTargetLanguage()
        );
        return Response.ok(response).build();
    }
}
