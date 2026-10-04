package com.github.techChallenge.application.repositories;

import com.github.techChallenge.domain.userlevel.UserLevel;
import org.springframework.data.domain.Page;

public interface IUserLevelRepository {
    UserLevel create(UserLevel userlevel);
    UserLevel update(UserLevel userlevel, int id);
    UserLevel findByID(int id);
    Page<UserLevel> list(int page, int offset);
    void delete(int id);
    boolean existsByTitle(String title);
    boolean existsByTitleAndIdNot(String title, int id);
    boolean existsById(int id);
}