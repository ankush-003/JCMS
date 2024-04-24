package com.example.postgres.config.jwt;

import com.example.postgres.config.RSAKeyRecord;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAccessTokenFilter extends OncePerRequestFilter {

    private final RSAKeyRecord rsaKeyRecord;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response,@NotNull FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("[JwtAccessTokenFilter:doFilterInternal] :: Started ");
            log.info("[JwtAccessTokenFilter:doFilterInternal] Filtering the Http Request:{}", request.getRequestURI());
            CustomFilter decodeFilter = new DecodeFilter(jwtTokenUtils, rsaKeyRecord);
            CustomFilter validHeaderCheckFilter = new ValidHeaderCheckFilter();
            CustomFilter validUserCheckFilter = new ValidUserCheckFilter(jwtTokenUtils);

            validHeaderCheckFilter.handle(request, response, filterChain);

            // Check if the request is already complete (e.g., if an error response was sent)
            if (response.isCommitted()) {
                return;
            }

            decodeFilter.handle(request, response, filterChain);

            // Check if the request is already complete
            if (response.isCommitted()) {
                return;
            }

            validUserCheckFilter.handle(request, response, filterChain);

            log.info("[JwtAccessTokenFilter:doFilterInternal] Completed");

        } catch (JwtValidationException jwtValidationException) {
            log.error("[JwtAccessTokenFilter:doFilterInternal] Exception due to :{}", jwtValidationException.getMessage());

            // Check if the exception is due to token expiration
            if (jwtValidationException.getMessage().contains("Jwt expired")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access token expired");
            } else {
                // Handle other JWT validation exceptions
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, jwtValidationException.getMessage());
            }
        }
    }
}
