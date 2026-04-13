package com.kenza.translator.auth;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Set;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class BasicAuthFilter implements ContainerRequestFilter {
    private static final Set<String> PUBLIC_PATHS = Set.of(
            "translator/health",
            "translator/info"
    );

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod()) || PUBLIC_PATHS.contains(path)) {
            return;
        }

        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            abort(requestContext, "Missing Authorization header.");
            return;
        }

        String base64Credentials = authHeader.substring("Basic ".length()).trim();
        String decoded;
        try {
            decoded = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            abort(requestContext, "Invalid Authorization header encoding.");
            return;
        }

        String[] values = decoded.split(":", 2);
        if (values.length != 2) {
            abort(requestContext, "Invalid basic auth format.");
            return;
        }

        String expectedUser = System.getenv().getOrDefault("APP_USERNAME", "admin");
        String expectedPass = System.getenv().getOrDefault("APP_PASSWORD", "admin123");

        if (!expectedUser.equals(values[0]) || !expectedPass.equals(values[1])) {
            abort(requestContext, "Invalid username or password.");
        }
    }

    private void abort(ContainerRequestContext requestContext, String message) {
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"DarijaTranslator\"")
                .entity(new ErrorMessage(message))
                .build());
    }

    public record ErrorMessage(String error) {}
}
