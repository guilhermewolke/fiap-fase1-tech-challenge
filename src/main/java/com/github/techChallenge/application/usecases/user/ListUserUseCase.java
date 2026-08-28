package com.github.techChallenge.application.usecases.user;

import com.github.techChallenge.application.gateways.IUserGateway;
import com.github.techChallenge.application.gateways.UserGateway;
import com.github.techChallenge.domain.user.IUserMapper;
import com.github.techChallenge.domain.user.User;
import com.github.techChallenge.domain.user.dto.UserCreateInputDTO;
import com.github.techChallenge.domain.user.dto.UserOutputDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public class ListUserUseCase extends UserUseCase {

    public ListUserUseCase(IUserGateway gateway, IUserMapper mapper) {
        super(gateway, mapper);
    }

    public Page<UserOutputDTO> execute(String name, int page, int offset) {
        Page<User> users = this.gateway.listByName(name, page, offset);
        return this.mapper.fromDomainPageToOutputDTOPage(users);
    }

    public Page<UserOutputDTO> execute(int page, int offset) {
        Page<User> users = this.gateway.list(page, offset);
        return this.mapper.fromDomainPageToOutputDTOPage(users);
    }
}
