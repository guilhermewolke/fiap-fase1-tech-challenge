package com.github.techChallenge.domain.user;

import com.github.techChallenge.domain.userlevel.UserLevel;

import java.time.LocalDateTime;
import java.util.Locale;

public class User {

    private Long id;
    private String name;
    private String email;
    private String login;
    private String password;
    private UserLevel level;
    private Address address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User() {
    }

    public User(
            Long id,
            String name,
            String email,
            String login,
            String password,
            UserLevel level,
            Address address,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.login = login;
        this.password = password;
        this.level = level;
        this.address = address;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User create(
            String name,
            String email,
            String login,
            String password,
            Address address
    ) {
        LocalDateTime now = LocalDateTime.now();
        email = email
                .trim()
                .toLowerCase(Locale.ROOT);

        login = login
                .trim()
                .toLowerCase(Locale.ROOT);

        return new User(
                null,
                name,
                email,
                login,
                password,
                null,
                address,
                now,
                now
        );
    }

    public void update(
            String name,
            String email,
            String login,
            Address address
    ) {

        email = email
                .trim()
                .toLowerCase(Locale.ROOT);

        login = login
                .trim()
                .toLowerCase(Locale.ROOT);

        this.name = name;
        this.email = email;
        this.login = login;
        this.address = address;
        this.updatedAt = LocalDateTime.now();
    }

    public void changePassword(String password) {
        this.password = password;
        this.updatedAt = LocalDateTime.now();
    }

    public void grantLevel(UserLevel userLevel) {
        this.level = userLevel;
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public UserLevel getLevel() {
        return level;
    }

    public Address getAddress() {
        return address;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

}