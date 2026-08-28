package com.github.techChallenge.infrastructure.security;

import jakarta.validation.constraints.NotBlank;

public interface ISecurityConfig {
    public String   passwordEncoder(String rawPassword, @NotBlank String login);
    public boolean  passwordValidate(String rawPassword, String encodedPassword);
}
