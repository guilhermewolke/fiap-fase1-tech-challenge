package com.github.techChallenge.application.exceptions;


public class RestaurantNotFoundException extends BusinessException {

    public RestaurantNotFoundException(Long id) {
        super("restaurant-not-found", "Restaurante não encontrado para o id " + id);
    }

    public RestaurantNotFoundException(String message) {
        super("restaurant-not-found", message);
    }
}
