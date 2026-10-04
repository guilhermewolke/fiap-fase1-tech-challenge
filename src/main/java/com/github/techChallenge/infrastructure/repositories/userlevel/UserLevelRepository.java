package com.github.techChallenge.infrastructure.repositories.userlevel;

import com.github.techChallenge.infrastructure.entities.userlevel.UserLevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLevelRepository extends JpaRepository<UserLevelEntity, Integer> {

    boolean existsByTitleIgnoreCase(String title);
    boolean existsByTitleAndIdNot(String email, int id);

}