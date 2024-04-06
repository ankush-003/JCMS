package com.example.postgres.config;

import com.example.postgres.config.jwt.JwtAccessTokenFilter;
import com.example.postgres.config.jwt.JwtRefreshTokenFilter;
import com.example.postgres.repository.RefreshTokenRepository;
import com.example.postgres.service.LogoutHandlerService;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.example.postgres.config.jwt.JwtTokenUtils;

import static org.springframework.security.config.Customizer.withDefaults;
import com.example.postgres.config.user.UserInfoManagerConfig;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final UserInfoManagerConfig userInfoManagerConfig;
    private final RSAKeyRecord rsaKeyRecord;
    private final JwtTokenUtils jwtTokenUtils;
    private final RefreshTokenRepository refreshTokenRepo;
    private final LogoutHandlerService logoutHandlerService;

    @Order(1)
    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        try {
            return httpSecurity
                    .securityMatcher(new AntPathRequestMatcher("/api/**"))
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                    .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(new JwtAccessTokenFilter(rsaKeyRecord, jwtTokenUtils), UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling((ex) -> ex
                            .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                            .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                    )
                    .formLogin(withDefaults())
                    .httpBasic(withDefaults())
                    .build();
        } catch (Exception ex) {
            log.error("[SecurityConfig:apiSecurityFilterChain] Exception occurred: {}", ex.getMessage(), ex);
            throw ex;
        }
    }


    @Order(2)
    @Bean
    public SecurityFilterChain signInSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        try {
            return httpSecurity
                    .securityMatcher(new AntPathRequestMatcher("/sign-in/**"))
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                    .userDetailsService(userInfoManagerConfig)
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .exceptionHandling(ex -> {
                        ex.authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage()));
                    })
                    .httpBasic(withDefaults())
                    .build();
        } catch (Exception ex) {
            log.error("[SecurityConfig:signInSecurityFilterChain] Exception occurred: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    @Order(3)
    @Bean
    public SecurityFilterChain refreshTokenSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        try {
            return httpSecurity
                    .securityMatcher(new AntPathRequestMatcher("/refresh-token/**"))
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                    .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                    .addFilterBefore(new JwtRefreshTokenFilter(rsaKeyRecord, jwtTokenUtils, refreshTokenRepo), UsernamePasswordAuthenticationFilter.class)
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .exceptionHandling(ex -> {
                        ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
                        ex.accessDeniedHandler(new BearerTokenAccessDeniedHandler());
                    })
                    .httpBasic(withDefaults())
                    .build();
        } catch (Exception ex) {
            log.error("[SecurityConfig:refreshTokenSecurityFilterChain] Exception occurred: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    @Order(4)
    @Bean
    public SecurityFilterChain logoutSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        try {
            return httpSecurity
                    .securityMatcher(new AntPathRequestMatcher("/logout/**"))
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                    .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(new JwtAccessTokenFilter(rsaKeyRecord, jwtTokenUtils), UsernamePasswordAuthenticationFilter.class)
                    .logout(logout -> logout
                            .logoutUrl("/logout")
                            .addLogoutHandler(logoutHandlerService)
                            .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()))
                    )
                    .exceptionHandling(ex -> {
                        ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
                        ex.accessDeniedHandler(new BearerTokenAccessDeniedHandler());
                    })
                    .build();
        } catch (Exception ex) {
            log.error("[SecurityConfig:logoutSecurityFilterChain] Exception occurred: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    @Order(5)
    @Bean
    public SecurityFilterChain registerSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        try {
            return httpSecurity
                    .securityMatcher(new AntPathRequestMatcher("/sign-up/**"))
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth ->
                            auth.anyRequest().permitAll())
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .build();
        } catch (Exception ex) {
            log.error("[SecurityConfig:registerSecurityFilterChain] Exception occurred: {}", ex.getMessage(), ex);
            throw ex;
        }
    }





    @Bean
    JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(rsaKeyRecord.rsaPublicKey()).build();
    }

    @Bean
    JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder(rsaKeyRecord.rsaPublicKey()).privateKey(rsaKeyRecord.rsaPrivateKey()).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

