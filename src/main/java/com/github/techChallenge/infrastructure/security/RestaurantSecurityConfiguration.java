package com.github.techChallenge.infrastructure.security;

import com.github.techChallenge.application.exceptions.ProblemDetailFactory;
import com.github.techChallenge.infrastructure.repositories.user.UserRepository;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import tools.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Locale;

@Configuration
@SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
public class RestaurantSecurityConfiguration {
    @Bean
    UserDetailsService restaurantUserDetailsService(UserRepository users) {
        return login -> users.findByLogin(login.trim().toLowerCase(Locale.ROOT))
                .map(RestaurantUserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas."));
    }

    @Bean PasswordEncoder restaurantPasswordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    SecurityFilterChain restaurantSecurityFilterChain(HttpSecurity http, ObjectMapper json,
                                                     ProblemDetailFactory problems) throws Exception {
        return http
                .securityMatcher("/api/v1/restaurant", "/api/v1/restaurant/**", "/api/v1/user/**")
                .csrf(AbstractHttpConfigurer::disable)
                .requestCache(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/v1/user", "/api/v1/user/", "/api/v1/user/auth-login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()
                        .requestMatchers(HttpMethod.HEAD, "/**").permitAll()
                        .requestMatchers("/api/v1/user/**").authenticated()
                        .anyRequest().hasRole("OWNER"))
                .httpBasic(basic -> basic.authenticationEntryPoint((request, response, exception) -> {
                    response.setHeader("WWW-Authenticate", "Basic realm=\"tech-challenge\"");
                    writeProblem(request, response, json, problems, HttpStatus.UNAUTHORIZED,
                            "Credenciais inválidas", "Informe um login e senha válidos.", "INVALID_CREDENTIALS");
                }))
                .exceptionHandling(errors -> errors
                        .authenticationEntryPoint((request, response, exception) -> {
                            response.setHeader("WWW-Authenticate", "Basic realm=\"tech-challenge\"");
                            writeProblem(request, response, json, problems, HttpStatus.UNAUTHORIZED,
                                    "Autenticação necessária", "Informe um login e senha válidos.", "AUTHENTICATION_REQUIRED");
                        })
                        .accessDeniedHandler((request, response, exception) -> writeProblem(request, response, json, problems,
                                HttpStatus.FORBIDDEN, "Acesso negado", "Somente donos de restaurante podem executar esta operação.", "FORBIDDEN_OPERATION")))
                .build();
    }

    private static void writeProblem(HttpServletRequest request, HttpServletResponse response, ObjectMapper json,
                                     ProblemDetailFactory problems, HttpStatus status, String title,
                                     String detail, String code) throws IOException {
        response.setStatus(status.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        json.writeValue(response.getOutputStream(), problems.create(status, title, detail, code, request));
    }
}
