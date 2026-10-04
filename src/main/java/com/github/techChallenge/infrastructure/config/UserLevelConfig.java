package com.github.techChallenge.infrastructure.config;

import com.github.techChallenge.application.gateways.userlevel.IUserLevelGateway;
import com.github.techChallenge.application.gateways.userlevel.UserLevelGateway;
import com.github.techChallenge.application.usecases.userlevel.*;
import com.github.techChallenge.application.validators.UserLevelValidator;
import com.github.techChallenge.domain.userlevel.IUserLevelMapper;
import com.github.techChallenge.infrastructure.mappers.UserLevelMapper;
import com.github.techChallenge.infrastructure.repositories.userlevel.UserLevelRepositoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserLevelConfig {

    @Bean
    CreateUserLevelUseCase createUserLevelUseCase(IUserLevelGateway gateway, IUserLevelMapper mapper) {
        return new CreateUserLevelUseCase(gateway, mapper);
    }

    @Bean
    UpdateUserLevelUseCase updateUserLevelUseCase(IUserLevelGateway gateway, UserLevelMapper mapper, UserLevelValidator userLevelValidator) {
        return new UpdateUserLevelUseCase(gateway, mapper, userLevelValidator);
    }

    @Bean
    FindUserLevelUseCase findUserLevelUseCase(IUserLevelGateway gateway, UserLevelMapper mapper) {
        return new FindUserLevelUseCase(gateway, mapper);
    }

    @Bean
    ListUserLevelUseCase listUserLevelUseCase(IUserLevelGateway gateway, UserLevelMapper mapper) {
        return new ListUserLevelUseCase(gateway, mapper);
    }

    @Bean
    DeleteUserLevelUseCase deleteUserLevelUseCase(IUserLevelGateway gateway, UserLevelMapper mapper) {
        return new DeleteUserLevelUseCase(gateway, mapper);
    }

    @Bean
    UserLevelValidator userLevelValidator(IUserLevelGateway gateway, IUserLevelMapper mapper) {
        return new UserLevelValidator(gateway, mapper);
    }

    @Bean
    UserLevelGateway userLevelGateway(UserLevelRepositoryGateway userLevelRepositoryGateway, UserLevelMapper userLevelMapper) {
        return new UserLevelGateway(userLevelRepositoryGateway, userLevelMapper);
    }

    @Bean
    UserLevelRepositoryGateway userLevelRepositoryGateway() {
        return new UserLevelRepositoryGateway();
    }

}
