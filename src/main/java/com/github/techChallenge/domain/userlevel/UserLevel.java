package com.github.techChallenge.domain.userlevel;

public class UserLevel {
    private int id;
    private String title;

    public UserLevel() {
    }

    public UserLevel(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public static UserLevel create(String title) {
        return new UserLevel(0, title);
    }
}
