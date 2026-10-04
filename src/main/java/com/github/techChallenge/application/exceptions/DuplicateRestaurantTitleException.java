package com.github.techChallenge.application.exceptions;

public class DuplicateRestaurantTitleException extends BusinessException {

    public DuplicateRestaurantTitleException(String title) {
        super("duplicate-title", "O nome de restaurante " + title + " já está cadastrado no sistema");
    }
}
