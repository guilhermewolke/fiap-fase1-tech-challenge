package com.github.techChallenge.application.exceptions;

public class DuplicateRestaurantCategoryTitleException extends BusinessException {

    public DuplicateRestaurantCategoryTitleException(String title) {
        super("duplicate-title", "O nome da categoria de restaurante " + title + " já está cadastrado no sistema");
    }
}
