package com.github.techChallenge.application.exceptions;

public class RestaurantCategoryNotFoundException extends BusinessException {

    public RestaurantCategoryNotFoundException(int id) {
        super("restaurant-category-not-found", "Tipo de restaurante não encontrado para o id " + id);
    }

    public RestaurantCategoryNotFoundException(String message) {
        super("restaurant-category-not-found", message);
    }
}
