package com.github.techChallenge.application.gateways.userlevel;

import com.github.techChallenge.application.repositories.IUserLevelRepository;
import com.github.techChallenge.domain.userlevel.IUserLevelMapper;
import com.github.techChallenge.domain.userlevel.UserLevel;
import org.springframework.data.domain.Page;

public class UserLevelGateway implements IUserLevelGateway {

    private final IUserLevelRepository repository;
    private final IUserLevelMapper mapper;

    public UserLevelGateway(IUserLevelRepository repository, IUserLevelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public UserLevel create(UserLevel user) {
        return repository.create(user);
    }

    @Override
    public UserLevel update(UserLevel user, int id) {
        user = this.repository.update(user, id);
        return user;
    }

    @Override
    public UserLevel find(int id) {
        UserLevel userLevel = this.repository.findByID(id);
        return userLevel;
    }

    @Override
    public Page<UserLevel> list(Integer page, Integer offset) {
        Page<UserLevel> users = this.repository.list(page, offset);

        return users;
    }

    @Override
    public void delete(int id) {
        this.repository.delete(id);
    }

    @Override
    public boolean titleExists(String title, int id) {
        return this.repository.existsByTitleAndIdNot(title, id);
    }

    @Override
    public boolean titleExists(String title) {
        return this.repository.existsByTitle(title);
    }

    @Override
    public boolean existById(int id) {
        return this.repository.existsById(id);
    }

}
