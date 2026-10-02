package com.github.techChallenge.infrastructure.repositories;

import com.github.techChallenge.application.exceptions.UserNotFoundException;
import com.github.techChallenge.application.repositories.IUserRepository;
import com.github.techChallenge.domain.user.IUserMapper;
import com.github.techChallenge.domain.user.User;
import com.github.techChallenge.infrastructure.entities.user.AddressEntity;
import com.github.techChallenge.infrastructure.entities.user.UserEntity;
import com.github.techChallenge.infrastructure.security.ISecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class UserRepositoryGateway implements IUserRepository {

    @Autowired
    private UserRepository repository;
    @Autowired
    private ISecurityConfig securityConfig;
    @Autowired
    private IUserMapper mapper;

    @Override
    public User create(User user) {
        UserEntity userEntity = new UserEntity(user);
        userEntity = this.repository.save(userEntity);
        return this.mapper.fromEntityToDomain(userEntity);
    }

    @Override
    public User update(User user, Long id) {

        Optional<UserEntity> entity = this.repository.findById(id);
        if (!entity.isPresent()) throw new UserNotFoundException("Usuário não encontrado");
        AddressEntity addressEntity = new AddressEntity(user.getAddress());

        UserEntity userEntity = entity.get();
        userEntity.setName(user.getName());
        userEntity.setLogin(user.getLogin());
        userEntity.setLevel(user.getLevel());
        userEntity.setEmail(user.getEmail());
        userEntity.setUpdatedAt(user.getUpdatedAt());
        userEntity.setAddress(addressEntity);

        userEntity = this.repository.save(userEntity);
        return this.mapper.fromEntityToDomain(userEntity);
    }

    @Override
    public Page<User> listByName(String name, int page, int offset) {
        Pageable pageable = PageRequest.of(page, offset);
        Page<UserEntity> entitiesPage =  this.repository.findByNameContainingIgnoreCase(name, pageable);

        return this.mapper.fromEntityPageToDomainPage(entitiesPage);
    }

    @Override
    public User findByID(Long id) {
        Optional<UserEntity> entity = this.repository.findById(id);
        if (!entity.isPresent()) throw new UserNotFoundException("Usuário não encontrado");

        return this.mapper.fromEntityToDomain(entity.get());
    }

    @Override
    public Page<User> list(int page, int offset) {
        Pageable pageable = PageRequest.of(page, offset);
        Page<UserEntity> entitiesPage =  this.repository.findAll(pageable);

        return this.mapper.fromEntityPageToDomainPage(entitiesPage);
    }

    @Override
    public void delete(Long id) {
        Optional<UserEntity> entity = this.repository.findById(id);
        if (!entity.isPresent()) throw new UserNotFoundException("Usuário não encontrado");

        this.repository.delete(entity.get());
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmailIgnoreCase(email);
    }

    @Override
    public boolean existsByLogin(String login) {
        return repository.existsByLoginIgnoreCase(login);
    }

    public String getEncryptPasswordByLogin(String login){
          return repository.findPasswordByLogin(login);
    }

    public boolean updatePasswordByLogin(String encryptedPassword, String login){
        Optional<UserEntity> entity = this.repository.findByLogin(login);
        if (!entity.isPresent()) {
            throw new UserNotFoundException("Usuário não encontrado");
        }

        UserEntity userEntity = entity.get();
        userEntity.setPassword(encryptedPassword);
        try {
            this.repository.save(userEntity);
            return true;
        } catch (IllegalArgumentException | OptimisticLockingFailureException e) {
            throw e;
        }

    }

    @Override
    public boolean existsByEmailAndIdNot(String email, Long id) {
        return this.repository.existsByEmailAndIdNot(email, id);
    }

    @Override
    public boolean existsByLoginAndIdNot(String login, Long id) {
        return this.repository.existsByLoginAndIdNot(login, id);
    }

    @Override
    public boolean existsById(Long id) {
        return this.repository.existsById(id);
    }
}
