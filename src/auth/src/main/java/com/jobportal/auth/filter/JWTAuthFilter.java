package com.jobportal.auth.filter;

import com.jobportal.auth.service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = jwtService.extractJWTTokenFromCookie(request);

        if(jwtToken != null && !jwtToken.isBlank()){
            try{
                Claims claims = jwtService.validateToken(jwtToken);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, List.of());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            catch(ExpiredJwtException ex){
                log.warn("JWT Token expired | path={}", request.getRequestURI());

                request.setAttribute("auth_error_messsage", "Authentication Token expired");
                request.setAttribute("auth_error_code", "TOKEN_EXPIRED");
            }
            catch (MalformedJwtException ex) {
                log.warn("Malformed JWT | path={}", request.getRequestURI());

                request.setAttribute("auth_error_code",    "TOKEN_MALFORMED");
                request.setAttribute("auth_error_messsage", "Token format is invalid");

            }
            catch (JwtException ex) {
                log.warn("Invalid JWT | path={} | error={}", request.getRequestURI(), ex.getMessage());

                request.setAttribute("auth_error_code",    "TOKEN_INVALID");
                request.setAttribute("auth_error_messsage", "Token is invalid");
            }
        }
        else{
            log.warn("JWT Token missing");

            request.setAttribute("auth_error_code",    "TOKEN_MISSING");
            request.setAttribute("auth_error_messsage", "Authentication Token is missing");
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        String path = request.getServletPath();

        return path.startsWith("/service/auth/login") || path.startsWith("/service/auth/signup") || path.startsWith("/service/user/validate");
    }
}
