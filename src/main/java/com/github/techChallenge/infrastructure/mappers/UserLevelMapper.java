package com.github.techChallenge.infrastructure.mappers;

import com.github.techChallenge.domain.user.Address;
import com.github.techChallenge.domain.user.IUserMapper;
import com.github.techChallenge.domain.user.User;
import com.github.techChallenge.domain.user.dto.UserOutputDTO;
import com.github.techChallenge.domain.userlevel.IUserLevelMapper;
import com.github.techChallenge.domain.userlevel.UserLevel;
import com.github.techChallenge.domain.userlevel.dto.UserLevelOutputDTO;
import com.github.techChallenge.infrastructure.entities.user.UserEntity;
import com.github.techChallenge.infrastructure.entities.userlevel.UserLevelEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserLevelMapper implements IUserLevelMapper {
    @Override
    public UserLevel fromEntityToDomain(UserLevelEntity userLevelEntity) {
        if (Optional.ofNullable(userLevelEntity).isPresent()) {
            return new UserLevel(
                    userLevelEntity.getId(),
                    userLevelEntity.getTitle());
        }
        return null;
    }

    @Override
    public UserLevelOutputDTO fromDomainToOutputDTO(UserLevel userLevel) {
        if (Optional.ofNullable(userLevel).isPresent()) {
            return new UserLevelOutputDTO(
                    userLevel.getId(),
                    userLevel.getTitle());
        }

        return null;
    }

    @Override
    public UserLevelEntity fromDomainToEntity(UserLevel domain) {
        if (Optional.ofNullable(domain).isPresent()) {
            return new UserLevelEntity(domain);
        }
        return null;
    }

    @Override
    public Page<UserLevel> fromEntityPageToDomainPage(Page<UserLevelEntity> entitiesPage) {
        return entitiesPage.map(u -> {
            return new UserLevel(
                u.getId(),
                u.getTitle());
        });
    }

    @Override
    public Page<UserLevelOutputDTO> fromDomainPageToOutputDTOPage(Page<UserLevel> domainPage) {
        return domainPage.map(u -> {
            return new UserLevelOutputDTO(
                u.getId(),
                u.getTitle());
        });
    }

}
