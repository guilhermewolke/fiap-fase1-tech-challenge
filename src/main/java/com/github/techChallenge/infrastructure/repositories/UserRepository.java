package com.github.techChallenge.infrastructure.repositories;

import com.github.techChallenge.infrastructure.entities.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository
        extends JpaRepository<UserEntity, Long> {

    Page<UserEntity> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByLoginIgnoreCase(String login);

    @Query(value = "SELECT SENHA FROM usuarios WHERE LOGIN = :login", nativeQuery = true)
    String findPasswordByLogin(@Param("login") String login);

    @Query(value = "SELECT * FROM usuarios WHERE login = :login", nativeQuery = true)
    Optional<UserEntity> findByLogin(@Param("login") String login);

    @Modifying
    @Transactional
    @Query(value = "UPDATE usuarios SET senha = :password WHERE login = :login", nativeQuery = true)
    int updatePasswordByLogin(@Param("login") String login, @Param("password") String password);

    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByLoginAndIdNot(String login, Long id);

}