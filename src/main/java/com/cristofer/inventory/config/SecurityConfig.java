package com.cristofer.inventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desactivamos CSRF porque no usamos cookies de sesión, usaremos tokens JWT
                .csrf(AbstractHttpConfigurer::disable)

                // Configuramos la aplicación para que no guarde el estado de la sesión (Stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Definimos las reglas de autorización de rutas
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas (Documentación OpenAPI / Swagger)
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // Cualquier otra petición (tus controladores de productos y categorías) requerirá autenticación
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}