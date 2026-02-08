package com.mlg.cementerio.security;

import org.springframework.http.HttpMethod;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // 1. RUTAS PÚBLICAS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/mock-redsys/**").permitAll()
                        .requestMatchers("/api/payment/callback/**").permitAll()
                        .requestMatchers("/api/ubicaciones/**").permitAll()

                        // 2. EMPRESAS
                        .requestMatchers(HttpMethod.GET, "/api/empresas/**").hasAnyRole("ADMIN", "EMPRESA", "CLIENTE")
                        .requestMatchers("/api/empresas/**").hasRole("ADMIN")

                        // 3. CEMENTERIOS
                        .requestMatchers(HttpMethod.GET, "/api/cementerios/**")
                        .hasAnyRole("ADMIN", "EMPRESA", "CLIENTE")
                        .requestMatchers("/api/cementerios/**").hasAnyRole("ADMIN", "EMPRESA")

                        // 4. SERVICIOS Y CEMENTERIO-SERVICIOS
                        .requestMatchers(HttpMethod.GET, "/api/servicios/catalogo").permitAll()
                   
                        .requestMatchers("/api/cementerio-servicios/**").hasAnyRole("ADMIN", "EMPRESA", "CLIENTE")
       
                        .requestMatchers("/api/servicios/cementerio/**").hasAnyRole("ADMIN", "EMPRESA")
                        .requestMatchers("/api/servicios/**").hasRole("ADMIN")

                        // 5. CONCESIONES Y DIFUNTOS
                        .requestMatchers(HttpMethod.POST, "/api/concesiones/**", "/api/difuntos/**")
                        .hasAnyRole("ADMIN", "CLIENTE", "EMPRESA")
                        .requestMatchers("/api/concesiones/**", "/api/difuntos/**")
                        .hasAnyRole("ADMIN", "EMPRESA", "CLIENTE")

                        // 6. RESTO
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:4200");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}