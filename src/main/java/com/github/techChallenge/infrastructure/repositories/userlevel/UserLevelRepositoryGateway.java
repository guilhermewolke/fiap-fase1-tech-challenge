package com.github.techChallenge.infrastructure.repositories.userlevel;

import com.github.techChallenge.application.exceptions.UserLevelNotFoundException;
import com.github.techChallenge.application.repositories.IUserLevelRepository;
import com.github.techChallenge.domain.userlevel.IUserLevelMapper;
import com.github.techChallenge.domain.userlevel.UserLevel;
import com.github.techChallenge.infrastructure.entities.userlevel.UserLevelEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class UserLevelRepositoryGateway implements IUserLevelRepository {

    @Autowired
    private UserLevelRepository repository;
    @Autowired
    private IUserLevelMapper mapper;

    @Override
    public UserLevel create(UserLevel userLevel) {
        UserLevelEntity userLevelEntity = new UserLevelEntity(userLevel);
        userLevelEntity = this.repository.save(userLevelEntity);
        return this.mapper.fromEntityToDomain(userLevelEntity);
    }

    @Override
    public UserLevel update(UserLevel userLevel, int id) {

        Optional<UserLevelEntity> entity = this.repository.findById(id);
        if (!entity.isPresent()) throw new UserLevelNotFoundException("Nível de usuário não encontrado");

        UserLevelEntity userLevelEntity = entity.get();
        userLevelEntity.setTitle(userLevel.getTitle());

        userLevelEntity = this.repository.save(userLevelEntity);
        return this.mapper.fromEntityToDomain(userLevelEntity);
    }

    @Override
    public UserLevel findByID(int id) {
        Optional<UserLevelEntity> entity = this.repository.findById(id);
        if (!entity.isPresent()) throw new UserLevelNotFoundException("Nível de usuário não encontrado");

        return this.mapper.fromEntityToDomain(entity.get());
    }

    @Override
    public Page<UserLevel> list(int page, int offset) {
        Pageable pageable = PageRequest.of(page, offset);
        Page<UserLevelEntity> entitiesPage =  this.repository.findAll(pageable);

        return this.mapper.fromEntityPageToDomainPage(entitiesPage);
    }

    @Override
    public void delete(int id) {
        Optional<UserLevelEntity> entity = this.repository.findById(id);
        if (!entity.isPresent()) throw new UserLevelNotFoundException("Nível de usuário não encontrado");

        this.repository.delete(entity.get());
    }

    @Override
    public boolean existsByTitle(String title) {
        return repository.existsByTitleIgnoreCase(title);
    }

    @Override
    public boolean existsByTitleAndIdNot(String title, int id) {
        return this.repository.existsByTitleAndIdNot(title, id);
    }

    @Override
    public boolean existsById(int id) {
        return this.repository.existsById(id);
    }
}
