package com.github.techChallenge.application.validators;

import com.github.techChallenge.application.exceptions.InvalidPasswordException;
import com.github.techChallenge.application.gateways.user.IUserGateway;
import com.github.techChallenge.application.usecases.user.UserUseCase;
import com.github.techChallenge.domain.user.IUserMapper;
import com.github.techChallenge.domain.user.dto.UserAuthInputDTO;
import com.github.techChallenge.infrastructure.security.ISecurityConfig;
import com.github.techChallenge.shared.UnauthorizedException;

public class UserValidator extends UserUseCase {

    private final ISecurityConfig security;

    public UserValidator(IUserGateway gateway, IUserMapper mapper, ISecurityConfig security){
        super(gateway, mapper);
        this.security = security;
    }

    public boolean authUser(UserAuthInputDTO dto){
        if (!gateway.loginExists(dto.login())) {throw new UnauthorizedException() {
        };}

        String encryptPasswordDataBase = this.gateway.getEncryptPasswordByLogin(dto.login());
        String rawPassword = dto.password();

        if(encryptPasswordDataBase.isEmpty() || rawPassword.isEmpty())
        {throw new InvalidPasswordException("Senha inválida e/ou vazia");}

        if(security.passwordValidate(rawPassword, encryptPasswordDataBase)
                && gateway.loginExists(dto.login())){
            return true;
        } else {
            throw new UnauthorizedException();
        }
    }

    public boolean emailExists(String email, Long id) {
        return this.gateway.emailExists(email, id);
    }

    public boolean emailExists(String email) {
        return this.gateway.emailExists(email);
    }


    public boolean loginExists(String login, Long id) {
        return this.gateway.loginExists(login, id);
    }

    public boolean loginExists(String login) {
        return this.gateway.loginExists(login);
    }

    public boolean existsByID(Long id) {
        return this.gateway.existById(id);
    }

}
