package com.github.techChallenge.application.usecases.user;

import com.github.techChallenge.application.gateways.IUserGateway;
import com.github.techChallenge.domain.user.IUserMapper;
import com.github.techChallenge.domain.user.dto.UserChangePasswordInputDTO;
import com.github.techChallenge.infrastructure.security.ISecurityConfig;
import com.github.techChallenge.shared.UnauthorizedException;

public class ChangePasswordUseCase extends UserUseCase{
    private final ISecurityConfig security;

    public ChangePasswordUseCase(IUserGateway gateway, IUserMapper mapper, ISecurityConfig security) {
        super(gateway, mapper);
        this.security = security;
    }

    public boolean ChangePassword(UserChangePasswordInputDTO dto){
        if (!gateway.loginExists(dto.login())) {throw new UnauthorizedException() {
        };}
        String passwordEnconded = security.passwordEncoder(dto.password(), dto.login());
        return this.gateway.changePassword(passwordEnconded, dto.login());
    }
}
