package com.github.techChallenge.application.usecases.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.techChallenge.application.gateways.IUserGateway;
import com.github.techChallenge.domain.user.Address;
import com.github.techChallenge.domain.user.IUserMapper;
import com.github.techChallenge.domain.user.User;
import com.github.techChallenge.domain.user.UserLevel;
import com.github.techChallenge.domain.user.dto.UserCreateInputDTO;
import com.github.techChallenge.domain.user.dto.UserOutputDTO;
import com.github.techChallenge.infrastructure.security.ISecurityConfig;
import com.github.techChallenge.shared.EmailAlreadyExistsException;
import com.github.techChallenge.shared.LoginAlreadyExistsException;
import io.swagger.v3.oas.annotations.media.Schema;
import org.junit.jupiter.api.AfterEach;
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
class CreateUserUseCaseTest {

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    @Mock
    private IUserGateway gateway;

    @Mock
    private IUserMapper mapper;

    @Mock
    private ISecurityConfig securityConfig;

    private User user;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {
        createUserUseCase = new CreateUserUseCase(gateway, mapper, securityConfig);
        Address address = new Address("Rua 1","123","N/A",
                "11235-800","Bairro","Cidade",
                "UF","Brasil");

        this.user = User.create("User 1","user@user.com.br","user",
                "password", UserLevel.CUSTOMER, address);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Deve criar um novo usuário no sistema")
    void shouldCreateANewUser() {
        Address address = new Address("Rua 1","123","N/A",
                "11235-800","Bairro","Cidade",
                "UF","Brasil");

        User createdUser = new User(1L, "User 1","user@user.com.br",
                "user","pitipiripitipiripitipó", UserLevel.CUSTOMER,
                address, LocalDateTime.now(), LocalDateTime.now());

        UserOutputDTO convertedToDTOUser = new UserOutputDTO(createdUser.getId(),
                        createdUser.getName(), createdUser.getEmail(), createdUser.getLogin(),
                        createdUser.getLevel(), address, createdUser.getCreatedAt(),
                        createdUser.getUpdatedAt());

        when(gateway.emailExists(any(String.class))).thenReturn(false);
        when(gateway.loginExists(any(String.class))).thenReturn(false);
        when(securityConfig.passwordEncoder(any(String.class), any(String.class))).thenReturn("pitipiripitipiripitipó");
        when(gateway.create(any(User.class))).thenReturn(createdUser);
        when(mapper.fromDomainToOutputDTO(createdUser)).thenReturn(convertedToDTOUser);

        UserCreateInputDTO inputDTO = new UserCreateInputDTO("User 1","user@user.com.br",
                "user","password", UserLevel.CUSTOMER, address);

        UserOutputDTO outputDTO = createUserUseCase.execute(inputDTO);

        assertNotNull(outputDTO.id());
        assertNotNull(outputDTO.createdAt());
        assertNotNull(outputDTO.updatedAt());
        assertEquals(createdUser.getEmail(), outputDTO.email());
        assertEquals(createdUser.getLogin(), outputDTO.login());
        assertEquals(createdUser.getLevel(), outputDTO.level());
        assertEquals(createdUser.getName(), outputDTO.name());
        assertEquals(createdUser.getAddress().address(), outputDTO.address().address());
        assertEquals(createdUser.getAddress().city(), outputDTO.address().city());
        assertEquals(createdUser.getAddress().complement(), outputDTO.address().complement());
        assertEquals(createdUser.getAddress().country(), outputDTO.address().country());
        assertEquals(createdUser.getAddress().neighborhood(), outputDTO.address().neighborhood());
        assertEquals(createdUser.getAddress().number(), outputDTO.address().number());
        assertEquals(createdUser.getAddress().state(), outputDTO.address().state());
        assertEquals(createdUser.getAddress().zipCode(), outputDTO.address().zipCode());
    }

    @Test
    @DisplayName("Deve retornar exceção por já existir um usuário com o email informado")
    void shouldThrowAnExceptionDueToNewUsersEmailAlreadyExists() {
        Address address = new Address("Rua 1","123","N/A",
                "11235-800","Bairro","Cidade",
                "UF","Brasil");

        User createdUser = new User(1L, "User 1","user@user.com.br",
                "user","pitipiripitipiripitipó", UserLevel.CUSTOMER,
                address, LocalDateTime.now(), LocalDateTime.now());

        UserOutputDTO convertedToDTOUser = new UserOutputDTO(createdUser.getId(),
                createdUser.getName(), createdUser.getEmail(), createdUser.getLogin(),
                createdUser.getLevel(), address, createdUser.getCreatedAt(),
                createdUser.getUpdatedAt());

        when(gateway.emailExists(any(String.class))).thenReturn(true);

        UserCreateInputDTO inputDTO = new UserCreateInputDTO("User 1","user@user.com.br",
                "user","password", UserLevel.CUSTOMER, address);
        assertThrows(EmailAlreadyExistsException.class, () -> createUserUseCase.execute(inputDTO));

    }

    @Test
    @DisplayName("Deve retornar exceção por já existir um usuário com o login informado")
    void shouldThrowAnExceptionDueToNewUsersLoginAlreadyExists() {
        Address address = new Address("Rua 1","123","N/A",
                "11235-800","Bairro","Cidade",
                "UF","Brasil");

        User createdUser = new User(1L, "User 1","user@user.com.br",
                "user","pitipiripitipiripitipó", UserLevel.CUSTOMER,
                address, LocalDateTime.now(), LocalDateTime.now());

        UserOutputDTO convertedToDTOUser = new UserOutputDTO(createdUser.getId(),
                createdUser.getName(), createdUser.getEmail(), createdUser.getLogin(),
                createdUser.getLevel(), address, createdUser.getCreatedAt(),
                createdUser.getUpdatedAt());

        when(gateway.emailExists(any(String.class))).thenReturn(false);
        when(gateway.loginExists(any(String.class))).thenReturn(true);

        UserCreateInputDTO inputDTO = new UserCreateInputDTO("User 1","user@user.com.br",
                "user","password", UserLevel.CUSTOMER, address);
        assertThrows(LoginAlreadyExistsException.class, () -> createUserUseCase.execute(inputDTO));
    }
}