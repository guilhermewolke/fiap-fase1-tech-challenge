package com.github.techChallenge.application.gateways.userlevel;

import com.github.techChallenge.domain.userlevel.UserLevel;
import org.springframework.data.domain.Page;

public interface IUserLevelGateway {
    UserLevel create(UserLevel userLevel);
    UserLevel update(UserLevel userLevel, int id);
    UserLevel find(int id);
    Page<UserLevel> list(Integer page, Integer offset);
    void delete(int id);
    boolean titleExists(String title, int id);
    boolean titleExists(String title);
    boolean existById(int id);
}
