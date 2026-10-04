package com.github.techChallenge.infrastructure.entities.userlevel;

import com.github.techChallenge.domain.userlevel.UserLevel;
import jakarta.persistence.*;

@Entity
@Table(name="usuarios_nivel")
public class UserLevelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "titulo", length = 50)
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {this.id = id;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserLevelEntity() {
    }

    public UserLevelEntity(UserLevel userLevel) {
        this.id = userLevel.getId();
        this.title = userLevel.getTitle();
    }
}
