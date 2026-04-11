package com.jobportal.auth.exception;

import com.jobportal.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Slf4j
@Component
public class JWTAuthEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Handles unauthorized access attempts by returning a structured JSON error response.
     * <p>
     * This entry point is triggered when an unauthenticated user attempts to access a protected
     * resource. It extracts specific error attributes set during the filter chain to provide
     * a detailed {@link ApiResponse} instead of a generic HTML error page.
     * </p>
     *
     * @param request       The incoming {@link HttpServletRequest}.
     * @param response      The {@link HttpServletResponse} to be populated with JSON.
     * @param authException The specific {@link AuthenticationException} that triggered this entry point.
     * @throws IOException  If an input or output exception occurs while writing to the response.
     */
    @Override
    public void commence(@NonNull HttpServletRequest request,
                         @NonNull HttpServletResponse response,
                         @NonNull AuthenticationException authException) throws IOException {
        String errorCode = getErrorCode(request);
        String errorMessage = getErrorMessage(request, authException);

        log.warn("Authorization failed | path: {} | reason: {}", request.getRequestURI(), authException.getMessage(), authException);

        ApiResponse<Void> apiResponse = new ApiResponse<>(errorMessage, HttpStatus.UNAUTHORIZED, false);
        apiResponse.setError(errorCode);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(mapper.writeValueAsString(apiResponse));
    }

    /**
     * Retrieves the specific error code associated with an authentication failure.
     * <p>
     * This method reads the error code that was previously stored as a
     * request attribute by the {@code JwtAuthFilter} during the
     * authentication process.
     * </p>
     *
     * @return a {@link String} representing the error code, or {@code null}
     *         if no error attribute is present.
     */
    private String getErrorCode(HttpServletRequest request) {
        String code = (String) request.getAttribute("auth_error_code");
        return code != null ? code : "UNAUTHORIZED";
    }

    /**
     * Retrieves the descriptive error message from the current request context.
     * <p>
     * This method extracts the human-readable error message that was stored
     * as a request attribute, typically during the authentication or
     * filtering process.
     * </p>
     *
     * @return a {@link String} containing the error details,
     *         or {@code null} if no message was stored.
     */
    private String getErrorMessage(
            HttpServletRequest request,
            AuthenticationException authException) {

        String message = (String) request.getAttribute("auth_error_message");
        return message != null ? message : authException.getMessage();
    }
}
