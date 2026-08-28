package com.github.techChallenge.infrastructure.security;


import jakarta.validation.constraints.NotBlank;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class SecurityConfig implements ISecurityConfig{

    public String passwordEncoder(String rawPassword, @NotBlank String login){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        return encoder.encode(rawPassword);}

    public boolean passwordValidate(String rawPassword, String encodedPassword){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        return encoder.matches(rawPassword,  encodedPassword);}
}
