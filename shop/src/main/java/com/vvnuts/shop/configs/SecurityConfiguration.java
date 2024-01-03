package com.vvnuts.shop.configs;

import com.vvnuts.shop.entities.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**", "/swagger-ui/**", "v3/api-docs/**",
            "/api/v1/item/filter"};
    private static final String[] WHITE_LIST_GET_URL = {"/api/v1/catalog/**", "/api/v1/characteristic/**",
            "/api/v1/character_item/**", "/api/v1/item/**", "/api/v1/order/**", "/api/v1/review/**", "/api/v1/image/**",
            "/api/v1/user/**"};
    private static final String[] WHITE_LIST_USER_URL = {"/api/v1/bucket/**", "/api/v1/user/**",
            "/api/v1/image/user/**", "/api/v1/review/**"};
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfig = new CorsConfiguration();
                    corsConfig.addAllowedOrigin("http://localhost:5173");
                    corsConfig.addAllowedMethod("*");
                    corsConfig.addAllowedHeader("*");
                    return corsConfig;
                }))
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        .requestMatchers(HttpMethod.GET, WHITE_LIST_GET_URL).permitAll()
                        .requestMatchers(WHITE_LIST_USER_URL).hasAnyAuthority(Role.ROLE_USER.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/order/**").hasAnyAuthority(Role.ROLE_USER.name())
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/order/**").hasAnyAuthority(Role.ROLE_USER.name())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/order/**").hasAnyAuthority(Role.ROLE_USER.name())
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/user/**").hasAnyAuthority(Role.ROLE_USER.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/user/**").hasAnyAuthority(Role.ROLE_USER.name())
                        .requestMatchers("/api/v1/**").hasAnyAuthority(Role.ROLE_ADMIN.name())
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
