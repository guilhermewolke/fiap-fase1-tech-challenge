package com.github.techChallenge.domain.restaurantCategory;

public class RestaurantCategory {
    private int id;
    private String title;

    public RestaurantCategory() {
    }

    public RestaurantCategory(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public static RestaurantCategory create(String title) {
        return new RestaurantCategory(0, title);
    }
}
