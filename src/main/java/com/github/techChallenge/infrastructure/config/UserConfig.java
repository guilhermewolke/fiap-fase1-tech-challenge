package com.github.techChallenge.infrastructure.config;

import com.github.techChallenge.application.gateways.IUserGateway;
import com.github.techChallenge.application.gateways.UserGateway;
import com.github.techChallenge.application.usecases.user.*;
import com.github.techChallenge.application.validators.UserValidator;
import com.github.techChallenge.domain.user.IUserMapper;
import com.github.techChallenge.infrastructure.mappers.UserMapper;
import com.github.techChallenge.infrastructure.repositories.UserRepositoryGateway;
import com.github.techChallenge.infrastructure.security.ISecurityConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Bean
    CreateUserUseCase createUserUseCase(IUserGateway gateway, IUserMapper mapper, ISecurityConfig securityConfig) {
        return new CreateUserUseCase(gateway, mapper, securityConfig);
    }

    @Bean
    UpdateUserUseCase updateUserUseCase(IUserGateway gateway, UserMapper mapper, UserValidator userValidator) {
        return new UpdateUserUseCase(gateway, mapper, userValidator);
    }

    @Bean
    FindUserUseCase findUserUseCase(IUserGateway gateway, UserMapper mapper) {
        return new FindUserUseCase(gateway, mapper);
    }

    @Bean
    ListUserUseCase listUserUseCase(IUserGateway gateway, UserMapper mapper) {
        return new ListUserUseCase(gateway, mapper);
    }

    @Bean
    DeleteUserUseCase deleteUserUseCase(IUserGateway gateway, UserMapper mapper) {
        return new DeleteUserUseCase(gateway, mapper);
    }

    @Bean
    UserValidator UserValidator(IUserGateway gateway, IUserMapper mapper, ISecurityConfig securityConfig) {
        return new UserValidator(gateway, mapper, securityConfig);
    }

    @Bean
    UserGateway userGateway(UserRepositoryGateway userRepositoryGateway, UserMapper userMapper, ISecurityConfig securityConfig) {
        return new UserGateway(userRepositoryGateway, userMapper, securityConfig);
    }

    @Bean
    UserRepositoryGateway userRepositoryGateway() {
        return new UserRepositoryGateway();
    }

    @Bean
    ChangePasswordUseCase changePasswordUser(IUserGateway gateway, IUserMapper mapper, ISecurityConfig securityConfig){return new ChangePasswordUseCase(
                gateway, mapper, securityConfig);};

}
