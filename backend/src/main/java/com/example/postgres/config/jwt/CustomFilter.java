package com.example.postgres.config.jwt;

import com.example.postgres.config.RSAKeyRecord;
import com.example.postgres.dto.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;

public interface CustomFilter {
    void handle(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException;
}

@RequiredArgsConstructor
@Slf4j
class ValidHeaderCheckFilter implements CustomFilter {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith(TokenType.Bearer.name())) {
            filterChain.doFilter(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
        }
    }
}



@RequiredArgsConstructor
@Slf4j
class DecodeFilter implements CustomFilter {
    private final JwtTokenUtils jwtTokenUtils;
    private final RSAKeyRecord rsaKeyRecord;



    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        JwtDecoder jwtDecoder =  NimbusJwtDecoder.withPublicKey(rsaKeyRecord.rsaPublicKey()).build();

        if (authHeader != null && authHeader.startsWith(TokenType.Bearer.name())) {
            final String token = authHeader.substring(7);
            final Jwt jwtToken = jwtDecoder.decode(token);
            final String userName = jwtTokenUtils.getUserName(jwtToken);

            // Set username and jwtToken in request attributes for later use
            request.setAttribute("username", userName);
            request.setAttribute("jwtToken", jwtToken);
        }

        filterChain.doFilter(request, response);
    }
}


@RequiredArgsConstructor
@Slf4j
class ValidUserCheckFilter implements CustomFilter {
    private final JwtTokenUtils jwtTokenUtils;



    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String userName = (String) request.getAttribute("username");
        final Jwt jwtToken = (Jwt) request.getAttribute("jwtToken");

        if (userName != null && jwtToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = jwtTokenUtils.userDetails(userName);
            if (userDetails != null && jwtTokenUtils.isTokenValid(jwtToken, userDetails)) {
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken createdToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                createdToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(createdToken);
                SecurityContextHolder.setContext(securityContext);
            }
        }

        filterChain.doFilter(request, response);
    }
}
