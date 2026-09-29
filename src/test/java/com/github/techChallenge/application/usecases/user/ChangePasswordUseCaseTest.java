package com.github.techChallenge.application.usecases.user;

import com.github.techChallenge.application.gateways.IUserGateway;
import com.github.techChallenge.domain.user.IUserMapper;
import com.github.techChallenge.domain.user.User;
import com.github.techChallenge.domain.user.dto.UserChangePasswordInputDTO;
import com.github.techChallenge.infrastructure.mappers.UserMapper;
import com.github.techChallenge.infrastructure.security.ISecurityConfig;
import com.github.techChallenge.shared.UnauthorizedException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChangePasswordUseCaseTest {

    @Mock
    private ISecurityConfig securityConfig;

    @Mock
    private IUserGateway gateway;

    @Mock
    private IUserMapper mapper;

    @InjectMocks
    private ChangePasswordUseCase changePasswordUseCase;

    private User user;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {
        changePasswordUseCase = new ChangePasswordUseCase(gateway, mapper, securityConfig);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    @DisplayName("Deve mudar a senha, uma vez que o login existe")
    void shouldChangePasswordOnceLoginExists() {
        when(gateway.loginExists("login")).thenReturn(true);
        when(securityConfig.passwordEncoder(any(String.class), any(String.class))).thenReturn("pitipiripitipiripitipó");
        when(gateway.changePassword("pitipiripitipiripitipó", "login")).thenReturn(true);

        UserChangePasswordInputDTO inputDTO = new UserChangePasswordInputDTO("login", "password");

        boolean changed = changePasswordUseCase.changePassword(inputDTO);
        assertTrue(changed);
    }

    @Test
    @DisplayName("Deve lançar exception se o login não existir")
    void shouldThrowAnExceptionIfLoginDoesNotExist() {
        when(gateway.loginExists("login")).thenReturn(false);

        UserChangePasswordInputDTO inputDTO = new UserChangePasswordInputDTO("login", "password");

        Assertions.assertThrows(UnauthorizedException.class, () -> changePasswordUseCase.changePassword(inputDTO));
    }
}