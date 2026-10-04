package com.github.techChallenge.application.exceptions;

public class UserLevelNotFoundException extends BusinessException {

    public UserLevelNotFoundException(int id) {
        super("userlevel-not-found", "Nível de Usuário não encontrado para o id " + id);
    }

    public UserLevelNotFoundException(String message) {
        super("userlevel-not-found", message);
    }
}
