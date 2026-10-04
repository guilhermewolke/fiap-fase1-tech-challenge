package com.github.techChallenge.application.exceptions;

public class DuplicateUserLevelTitleException extends BusinessException {

    public DuplicateUserLevelTitleException(String title) {
        super("duplicate-title", "O nome do nível de usuário " + title + " já está cadastrado no sistema");
    }
}
