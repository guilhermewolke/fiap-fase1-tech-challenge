package com.github.techChallenge.application.gateways;

import com.github.techChallenge.domain.user.User;
import com.github.techChallenge.domain.user.dto.UserCreateInputDTO;
import com.github.techChallenge.domain.user.dto.UserUpdateInputDTO;
import org.springframework.data.domain.Page;

public interface IUserGateway {
    User create(User user);
    User update(User user, Long id);
    User find(Long id);
    Page<User> listByName(String name, int page, int offset);
    Page<User> list(Integer page, Integer offset);
    void delete(Long id);
    String getEncryptPasswordByLogin(String login);
    boolean emailExists(String email, Long id);
    boolean loginExists(String login);
    boolean loginExists(String login, Long id);
    boolean existById(Long id);
    boolean changePassword(String rawPassword, String login);

}
