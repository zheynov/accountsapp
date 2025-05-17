package by.redlaw.acocuntsapp.exception;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

/**
 * To handle unauthorized access attempts to the application.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        int status = HttpServletResponse.SC_UNAUTHORIZED;

        if (authException instanceof InsufficientAuthenticationException) {
            status = HttpServletResponse.SC_FORBIDDEN;
        } else if (authException instanceof BadCredentialsException) {
            status = HttpServletResponse.SC_UNAUTHORIZED;
        }

        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> errorBody = Map.of(
                "status", status,
                "error", HttpStatus.valueOf(status).getReasonPhrase(),
                "message", "Authentication is required or invalid",
                "timestamp", Instant.now().toString()
        );

        objectMapper.writeValue(response.getOutputStream(), errorBody);
    }
}

