package com.github.techChallenge.domain.userlevel;

import com.github.techChallenge.domain.userlevel.dto.UserLevelOutputDTO;
import com.github.techChallenge.infrastructure.entities.userlevel.UserLevelEntity;
import org.springframework.data.domain.Page;

public interface IUserLevelMapper {
    UserLevel fromEntityToDomain(UserLevelEntity userLevelEntity);
    UserLevelOutputDTO fromDomainToOutputDTO(UserLevel user);
    UserLevelEntity fromDomainToEntity(UserLevel domain);
    Page<UserLevel> fromEntityPageToDomainPage(Page<UserLevelEntity> entitiesPage);
    Page<UserLevelOutputDTO> fromDomainPageToOutputDTOPage(Page<UserLevel> domainPage);
}
