package com.github.techChallenge.application.usecases.userlevel;

import com.github.techChallenge.application.gateways.userlevel.IUserLevelGateway;
import com.github.techChallenge.domain.userlevel.IUserLevelMapper;
import com.github.techChallenge.domain.userlevel.UserLevel;
import com.github.techChallenge.domain.userlevel.dto.UserLevelOutputDTO;
import org.springframework.data.domain.Page;

public class ListUserLevelUseCase extends UserLevelUseCase {

    public ListUserLevelUseCase(IUserLevelGateway gateway, IUserLevelMapper mapper) {
        super(gateway, mapper);
    }

    public Page<UserLevelOutputDTO> execute(int page, int offset) {
        Page<UserLevel> userLevels = this.gateway.list(page, offset);
        return this.mapper.fromDomainPageToOutputDTOPage(userLevels);
    }
}
