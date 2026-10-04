package com.github.techChallenge.application.usecases.user;

import com.github.techChallenge.application.exceptions.DuplicateEmailException;
import com.github.techChallenge.application.exceptions.DuplicateLoginException;
import com.github.techChallenge.application.exceptions.UserNotFoundException;
import com.github.techChallenge.application.gateways.user.IUserGateway;
import com.github.techChallenge.application.validators.UserValidator;
import com.github.techChallenge.domain.user.Address;
import com.github.techChallenge.domain.user.IUserMapper;
import com.github.techChallenge.domain.user.User;
import com.github.techChallenge.domain.userlevel.UserLevel;
import com.github.techChallenge.domain.user.dto.UserOutputDTO;
import com.github.techChallenge.domain.user.dto.UserUpdateInputDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
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

        this.user = new User(1L, "User 1", "user@user.com.br", "user", "password",
                null, address, LocalDateTime.now(), LocalDateTime.now());

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
            null,
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
            null,
            mockUpdatedUser.getAddress(),
            mockUpdatedUser.getCreatedAt(),
            mockUpdatedUser.getUpdatedAt());

        when(gateway.existById(any(Long.class))).thenReturn(true);
        when(validator.emailExists(any(String.class), any(Long.class))).thenReturn(false);
        when(validator.loginExists(any(String.class), any(Long.class))).thenReturn(false);
        when(gateway.update(any(User.class), any(Long.class))).thenReturn(mockUpdatedUser);
        when(mapper.fromDomainToOutputDTO(mockUpdatedUser)).thenReturn(convertedOutputDTO);

        UserUpdateInputDTO inputDTO = new UserUpdateInputDTO("User 1","user@user.com.br",
                "user", new Address("Rua 2","1234","N/A alterado",
                "11235-900","Bairro alterado","Cidade alterado",
                "UF alterado","Argentinha"));

        UserOutputDTO outputDTO = updateUserUseCase.execute(inputDTO, 1L);

        assertNotNull(outputDTO.id());
        assertNotNull(outputDTO.createdAt());
        assertNotNull(outputDTO.updatedAt());
        assertNotEquals(outputDTO.createdAt(), outputDTO.updatedAt());
        Assertions.assertEquals(mockUpdatedUser.getEmail(), outputDTO.email());
        Assertions.assertEquals(mockUpdatedUser.getLogin(), outputDTO.login());
        Assertions.assertEquals(mockUpdatedUser.getName(), outputDTO.name());
        Assertions.assertEquals(mockUpdatedUser.getAddress().address(), outputDTO.address().address());
        Assertions.assertEquals(mockUpdatedUser.getAddress().city(), outputDTO.address().city());
        Assertions.assertEquals(mockUpdatedUser.getAddress().complement(), outputDTO.address().complement());
        Assertions.assertEquals(mockUpdatedUser.getAddress().country(), outputDTO.address().country());
        Assertions.assertEquals(mockUpdatedUser.getAddress().neighborhood(), outputDTO.address().neighborhood());
        Assertions.assertEquals(mockUpdatedUser.getAddress().number(), outputDTO.address().number());
        Assertions.assertEquals(mockUpdatedUser.getAddress().state(), outputDTO.address().state());
        Assertions.assertEquals(mockUpdatedUser.getAddress().zipCode(), outputDTO.address().zipCode());
    }

    @Test
    @DisplayName("Deve lançar exceção caso não exista um usuário com o ID informado")
    void shouldThrowAnExceptionWhenThereIsNotAnyUserWithThisID() {
        User mockUpdatedUser = new User(
                user.getId(),
                "Nome alterado",
                "email alterado",
                "login alterado",
                "password alterado",
                null,
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
                null,
                mockUpdatedUser.getAddress(),
                mockUpdatedUser.getCreatedAt(),
                mockUpdatedUser.getUpdatedAt());

        when(gateway.existById(any(Long.class))).thenReturn(false);

        UserUpdateInputDTO inputDTO = new UserUpdateInputDTO("User 1","user@user.com.br",
                "user", new Address("Rua 2","1234","N/A alterado",
                "11235-900","Bairro alterado","Cidade alterado",
                "UF alterado","Argentinha"));

        assertThrows(UserNotFoundException.class, () -> updateUserUseCase.execute(inputDTO, 1L));

    }

    @Test
    @DisplayName("Deve lançar exceção caso exista outro usuário com o email informando")
    void shouldThrowAnExceptionWhenThereIsAlreadyAnotherUserWithThisEmail() {
        User mockUpdatedUser = new User(
                user.getId(),
                "Nome alterado",
                "email alterado",
                "login alterado",
                "password alterado",
                null,
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
                null,
                mockUpdatedUser.getAddress(),
                mockUpdatedUser.getCreatedAt(),
                mockUpdatedUser.getUpdatedAt());

        when(gateway.existById(any(Long.class))).thenReturn(true);
        when(validator.emailExists(any(String.class), any(Long.class))).thenReturn(true);

        UserUpdateInputDTO inputDTO = new UserUpdateInputDTO("User 1","user@user.com.br",
                "user", new Address("Rua 2","1234","N/A alterado",
                "11235-900","Bairro alterado","Cidade alterado",
                "UF alterado","Argentinha"));

        assertThrows(DuplicateEmailException.class, () -> updateUserUseCase.execute(inputDTO, 1L));
    }

    @Test
    @DisplayName("Deve lançar exceção caso exista outro usuário com o login informando")
    void shouldThrowAnExceptionWhenThereIsAlreadyAnotherUserWithThisLogin() {
        User mockUpdatedUser = new User(
                user.getId(),
                "Nome alterado",
                "email alterado",
                "login alterado",
                "password alterado",
                null,
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
                null,
                mockUpdatedUser.getAddress(),
                mockUpdatedUser.getCreatedAt(),
                mockUpdatedUser.getUpdatedAt());

        when(gateway.existById(any(Long.class))).thenReturn(true);
        when(validator.emailExists(any(String.class), any(Long.class))).thenReturn(false);
        when(validator.loginExists(any(String.class), any(Long.class))).thenReturn(true);

        UserUpdateInputDTO inputDTO = new UserUpdateInputDTO("User 1","user@user.com.br",
                "user", new Address("Rua 2","1234","N/A alterado",
                "11235-900","Bairro alterado","Cidade alterado",
                "UF alterado","Argentinha"));

        assertThrows(DuplicateLoginException.class, () -> updateUserUseCase.execute(inputDTO, 1L));
    }
}