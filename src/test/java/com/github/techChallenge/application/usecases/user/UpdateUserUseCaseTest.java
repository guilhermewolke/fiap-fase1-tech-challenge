package com.github.techChallenge.application.usecases.user;

import com.github.techChallenge.application.gateways.IUserGateway;
import com.github.techChallenge.application.validators.UserValidator;
import com.github.techChallenge.domain.user.Address;
import com.github.techChallenge.domain.user.IUserMapper;
import com.github.techChallenge.domain.user.User;
import com.github.techChallenge.domain.user.UserLevel;
import com.github.techChallenge.domain.user.dto.UserCreateInputDTO;
import com.github.techChallenge.domain.user.dto.UserOutputDTO;
import com.github.techChallenge.domain.user.dto.UserUpdateInputDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseTest {

    @InjectMocks
    private UpdateUserUseCase updateUserUseCase;

    @Mock
    private IUserGateway gateway;

    @Mock
    private IUserMapper mapper;

    @Mock
    private UserValidator validator;

    private User user;

    @BeforeEach
    void setUp() {
        updateUserUseCase = new UpdateUserUseCase(gateway, mapper, validator);
        Address address = new Address("Rua 1","123","N/A",
                "11235-800","Bairro","Cidade",
                "UF","Brasil");

        this.user = User.create("User 1","user@user.com.br","user",
                "password", UserLevel.CUSTOMER, address);
    }

    @Test
    @DisplayName("Deve alterar os dados do usuário")
    void shouldUpdateAUserData() {
        User mockUpdatedUser = new User(
            user.getId(),
            "Nome alterado",
            "email alterado",
            "login alterado",
            "password alterado",
            UserLevel.OWNER,
            new Address("Rua 2","1234","N/A alterado",
                    "11235-900","Bairro alterado","Cidade alterado",
                    "UF alterado","Argentinha"),
            user.getCreatedAt(),
            LocalDateTime.now()
        );

        UserOutputDTO convertedOutputDTO = new UserOutputDTO(
            mockUpdatedUser.getId(),
            mockUpdatedUser.getName(),
            mockUpdatedUser.getEmail(),
            mockUpdatedUser.getLogin(),
            mockUpdatedUser.getLevel(),
            mockUpdatedUser.getAddress(),
            mockUpdatedUser.getCreatedAt(),
            mockUpdatedUser.getUpdatedAt());

        when(gateway.existById(any(Long.class))).thenReturn(true);
        when(validator.emailExists(any(String.class), any(Long.class))).thenReturn(false);
        when(validator.loginExists(any(String.class), any(Long.class))).thenReturn(false);
        when(gateway.update(any(User.class), any(Long.class))).thenReturn(mockUpdatedUser);
        when(mapper.fromDomainToOutputDTO(mockUpdatedUser)).thenReturn(convertedOutputDTO);

        UserUpdateInputDTO inputDTO = new UserCreateInputDTO("User 1","user@user.com.br",
                "user","password", UserLevel.CUSTOMER, address);
    }

    @Test
    @DisplayName("Deve lançar exceção caso não exista um usuário com o ID informado")
    void shouldThrowAnExceptionWhenThereIsNotAnyUserWithThisID() {

    }

    @Test
    @DisplayName("Deve lançar exceção caso exista outro usuário com o email informando")
    void shouldThrowAnExceptionWhenThereIsAlreadyAnotherUserWithThisEmail() {

    }

    @Test
    @DisplayName("Deve lançar exceção caso exista outro usuário com o login informando")
    void shouldThrowAnExceptionWhenThereIsAlreadyAnotherUserWithThisLogin() {

    }
}