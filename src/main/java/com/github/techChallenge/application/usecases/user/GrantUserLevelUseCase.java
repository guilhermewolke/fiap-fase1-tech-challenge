package com.github.techChallenge.application.usecases.user;

import com.github.techChallenge.application.exceptions.UserLevelNotFoundException;
import com.github.techChallenge.application.exceptions.UserNotFoundException;
import com.github.techChallenge.application.gateways.user.IUserGateway;
import com.github.techChallenge.application.validators.UserLevelValidator;
import com.github.techChallenge.application.validators.UserValidator;
import com.github.techChallenge.domain.user.IUserMapper;
import com.github.techChallenge.domain.user.User;
import com.github.techChallenge.domain.user.dto.GrantUserLevelInputDTO;
import com.github.techChallenge.domain.user.dto.UserOutputDTO;
import com.github.techChallenge.domain.userlevel.UserLevel;
import com.github.techChallenge.domain.userlevel.dto.UserLevelOutputDTO;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

public class GrantUserLevelUseCase extends UserUseCase {
    private final UserLevelValidator userLevelValidator;
    private final UserValidator userValidator;

    public GrantUserLevelUseCase(IUserGateway gateway, IUserMapper mapper, UserValidator userValidator,
                                 UserLevelValidator userLevelValidator) {
        super(gateway, mapper);
        this.userValidator = userValidator;
        this.userLevelValidator = userLevelValidator;
    }

    public UserOutputDTO execute(GrantUserLevelInputDTO dto) {

        if (!this.userValidator.existsByID(dto.userID()))
            throw new UserNotFoundException("Usuário não encontrado");

        if (!this.userLevelValidator.existsByID(dto.userLevelID()))
            throw new UserLevelNotFoundException("Nível de usuário não encontrado pelo ID");

        User user = this.gateway.grantUserLevel(dto);
        return this.mapper.fromDomainToOutputDTO(user);
    }
}
