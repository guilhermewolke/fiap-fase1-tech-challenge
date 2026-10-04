package com.github.techChallenge.infrastructure.mappers;

import com.github.techChallenge.domain.user.Address;
import com.github.techChallenge.domain.user.IUserMapper;
import com.github.techChallenge.domain.user.User;
import com.github.techChallenge.domain.user.dto.UserOutputDTO;
import com.github.techChallenge.domain.userlevel.IUserLevelMapper;
import com.github.techChallenge.infrastructure.entities.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements IUserMapper {

    @Autowired
    private UserLevelMapper userLevelMapper;
    @Override
    public User fromEntityToDomain(UserEntity userEntity) {
        Address address = new Address(
            userEntity.getAddress().getAddress(),
            userEntity.getAddress().getNumber(),
            userEntity.getAddress().getComplement(),
            userEntity.getAddress().getZipCode(),
            userEntity.getAddress().getNeighborhood(),
            userEntity.getAddress().getCity(),
            userEntity.getAddress().getState(),
            userEntity.getAddress().getCountry()
        );

        return new User(
            userEntity.getId(),
            userEntity.getName(),
            userEntity.getEmail(),
            userEntity.getLogin(),
            userEntity.getPassword(),
            userLevelMapper.fromEntityToDomain(userEntity.getLevel()),
            address,
            userEntity.getCreatedAt(),
            userEntity.getUpdatedAt()
        );

    }

    @Override
    public UserOutputDTO fromDomainToOutputDTO(User user) {
        return new UserOutputDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getLogin(),
            userLevelMapper.fromDomainToOutputDTO(user.getLevel()),
            user.getAddress(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }

    @Override
    public Page<User> fromEntityPageToDomainPage(Page<UserEntity> entitiesPage) {
        return entitiesPage.map(u -> {
            Address address = new Address(
                    u.getAddress().getAddress(),
                    u.getAddress().getNumber(),
                    u.getAddress().getComplement(),
                    u.getAddress().getZipCode(),
                    u.getAddress().getNeighborhood(),
                    u.getAddress().getCity(),
                    u.getAddress().getState(),
                    u.getAddress().getCountry()
            );

            return new User(
                    u.getId(),
                    u.getName(),
                    u.getEmail(),
                    u.getLogin(),
                    null,
                    userLevelMapper.fromEntityToDomain(u.getLevel()),
                    address,
                    u.getCreatedAt(),
                    u.getUpdatedAt()
            );
        });
    }

    @Override
    public Page<UserOutputDTO> fromDomainPageToOutputDTOPage(Page<User> domainPage) {
        return domainPage.map(u -> {
            return new UserOutputDTO(
                u.getId(),
                u.getName(),
                u.getEmail(),
                u.getLogin(),
                userLevelMapper.fromDomainToOutputDTO(u.getLevel()),
                u.getAddress(),
                u.getCreatedAt(),
                u.getUpdatedAt()
            );
        });
    }

}
