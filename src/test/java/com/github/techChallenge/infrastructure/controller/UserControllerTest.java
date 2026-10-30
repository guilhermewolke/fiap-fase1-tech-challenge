package com.github.techChallenge.infrastructure.controller;

import com.github.techChallenge.application.exceptions.DuplicateEmailException;
import com.github.techChallenge.application.exceptions.GlobalExceptionHandler;
import com.github.techChallenge.application.exceptions.ProblemDetailFactory;
import com.github.techChallenge.application.exceptions.UserNotFoundException;
import com.github.techChallenge.application.usecases.user.*;
import com.github.techChallenge.application.validators.UserValidator;
import com.github.techChallenge.domain.user.Address;
import com.github.techChallenge.domain.user.InvalidUserDataException;
import com.github.techChallenge.domain.user.UserLevel;
import com.github.techChallenge.domain.user.dto.*;
import com.github.techChallenge.domain.usertype.dto.UserTypeOutputDTO;
import com.github.techChallenge.infrastructure.controllers.user.UserController;
import com.github.techChallenge.infrastructure.security.RestaurantUserPrincipal;
import com.github.techChallenge.shared.UnauthorizedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    private static final Long USER_ID = 7L;
    private static final String LOGIN = "vinicius.qa";
    private static final String USER_JSON = """
            {
              "name": "Vinicius",
              "email": "vinicius.qa@example.com",
              "login": "vinicius.qa",
              "password": "Senha@123",
              "userLevel": "CUSTOMER",
              "address": {
                "address": "Rua A", "number": "10", "complement": "Casa",
                "zipCode": "01000-000", "neighborhood": "Centro",
                "city": "São Paulo", "state": "SP", "country": "Brasil"
              }
            }
            """;
    private static final String AUTH_JSON = """
            {"login":"vinicius.qa","password":"Senha@123"}
            """;
    private static final String PASSWORD_JSON = """
            {"login":"vinicius.qa","password":"NovaSenha@123"}
            """;

    @Mock private CreateUserUseCase create;
    @Mock private UpdateUserUseCase update;
    @Mock private FindUserUseCase find;
    @Mock private ListUserUseCase list;
    @Mock private DeleteUserUseCase delete;
    @Mock private UserValidator validator;
    @Mock private ChangePasswordUseCase changePassword;
    @Mock private GrantUserTypeUseCase grantType;
    @InjectMocks private UserController controller;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler(new ProblemDetailFactory()))
                .build();
    }

    @AfterEach
    void clearAuthentication() {
        SecurityContextHolder.clearContext();
    }

    private UserOutputDTO output() {
        var now = LocalDateTime.of(2026, 10, 1, 10, 0);
        return new UserOutputDTO(USER_ID, "Vinicius", "vinicius.qa@example.com", LOGIN,
                UserLevel.CUSTOMER, new UserTypeOutputDTO(3, "Cliente"),
                new Address("Rua A", "10", "Casa", "01000-000", "Centro", "São Paulo", "SP", "Brasil"), now, now);
    }

    private RestaurantUserPrincipal authenticate(Long userId) {
        RestaurantUserPrincipal principal = mock(RestaurantUserPrincipal.class);
        when(principal.getId()).thenReturn(userId);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, null, List.of()));
        return principal;
    }

    private void authenticateForPasswordChange() {
        RestaurantUserPrincipal principal = authenticate(USER_ID);
        when(principal.getUsername()).thenReturn(LOGIN);
    }

    @Test
    void shouldCreateAUserWith201LocationAndTheReturnedBody() throws Exception {
        when(create.execute(any(UserCreateInputDTO.class))).thenReturn(output());

        mvc.perform(post("/api/v1/user/").contentType(MediaType.APPLICATION_JSON).content(USER_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/user/7"))
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.login").value(LOGIN));

        var input = ArgumentCaptor.forClass(UserCreateInputDTO.class);
        verify(create).execute(input.capture());
        assertEquals("Vinicius", input.getValue().name());
        assertEquals(UserLevel.CUSTOMER, input.getValue().userLevel());
        assertEquals("São Paulo", input.getValue().address().city());
    }

    @Test
    void shouldExposeDomainValidationFailuresWith400() throws Exception {
        when(create.execute(any(UserCreateInputDTO.class)))
                .thenThrow(new InvalidUserDataException(Map.of("name", "Nome inválido")));

        mvc.perform(post("/api/v1/user/").contentType(MediaType.APPLICATION_JSON).content(USER_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.violations.name").value("Nome inválido"));
    }

    @Test
    void shouldExposeDuplicateEmailWith409() throws Exception {
        when(create.execute(any(UserCreateInputDTO.class)))
                .thenThrow(new DuplicateEmailException("vinicius.qa@example.com"));

        mvc.perform(post("/api/v1/user/").contentType(MediaType.APPLICATION_JSON).content(USER_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void shouldRejectMalformedJsonBeforeCallingTheCreateUseCase() throws Exception {
        mvc.perform(post("/api/v1/user/").contentType(MediaType.APPLICATION_JSON).content("{invalido"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(create);
    }

    @Test
    void shouldFindAUserPublicly() throws Exception {
        when(find.execute(USER_ID)).thenReturn(output());

        mvc.perform(get("/api/v1/user/{id}", USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.email").value("vinicius.qa@example.com"));

        verify(find).execute(USER_ID);
    }

    @Test
    void shouldReturn404WhenTheRequestedUserDoesNotExist() throws Exception {
        when(find.execute(99L)).thenThrow(new UserNotFoundException(99L));

        mvc.perform(get("/api/v1/user/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void shouldListUsersWithTheDefaultPagination() throws Exception {
        when(list.execute(0, 10)).thenReturn(new PageImpl<>(List.of(output()), PageRequest.of(0, 10), 1));

        mvc.perform(get("/api/v1/user/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(7))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10));

        verify(list).execute(0, 10);
    }

    @Test
    void shouldForwardCustomPaginationWhenListingUsers() throws Exception {
        when(list.execute(2, 4)).thenReturn(new PageImpl<>(List.of(output()), PageRequest.of(2, 4), 9));

        mvc.perform(get("/api/v1/user/").param("page", "2").param("offset", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(2))
                .andExpect(jsonPath("$.size").value(4))
                .andExpect(jsonPath("$.totalElements").value(9));

        verify(list).execute(2, 4);
    }

    @Test
    void shouldUseTheNameSearchOverloadWithDefaultPagination() throws Exception {
        when(list.execute("Vinicius", 0, 10))
                .thenReturn(new PageImpl<>(List.of(output()), PageRequest.of(0, 10), 1));

        mvc.perform(get("/api/v1/user/find-by-name").param("name", "Vinicius"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Vinicius"));

        verify(list).execute("Vinicius", 0, 10);
        verify(list, never()).execute(anyInt(), anyInt());
    }

    @Test
    void shouldReturnAnEmptyNameSearchWithItsRequestedPagination() throws Exception {
        when(list.execute("Ausente", 1, 3)).thenReturn(Page.empty(PageRequest.of(1, 3)));

        mvc.perform(get("/api/v1/user/find-by-name")
                        .param("name", "Ausente").param("page", "1").param("offset", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.totalElements").value(0));

        verify(list).execute("Ausente", 1, 3);
    }

    @Test
    void shouldUpdateTheAuthenticatedUsersOwnAccount() throws Exception {
        authenticate(USER_ID);
        when(update.execute(any(UserUpdateInputDTO.class), eq(USER_ID))).thenReturn(output());

        mvc.perform(put("/api/v1/user/{id}", USER_ID).contentType(MediaType.APPLICATION_JSON).content(USER_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7));

        var input = ArgumentCaptor.forClass(UserUpdateInputDTO.class);
        verify(update).execute(input.capture(), eq(USER_ID));
        assertEquals(LOGIN, input.getValue().login());
        assertEquals("São Paulo", input.getValue().address().city());
    }

    @Test
    void shouldRejectUpdatingAnotherAccountBeforeCallingTheUseCase() throws Exception {
        authenticate(8L);

        mvc.perform(put("/api/v1/user/{id}", USER_ID).contentType(MediaType.APPLICATION_JSON).content(USER_JSON))
                .andExpect(status().isForbidden());

        verifyNoInteractions(update);
    }

    @Test
    void shouldRejectAnUpdateWithoutAnAuthenticatedPrincipal() throws Exception {
        mvc.perform(put("/api/v1/user/{id}", USER_ID).contentType(MediaType.APPLICATION_JSON).content(USER_JSON))
                .andExpect(status().isForbidden());

        verifyNoInteractions(update);
    }

    @Test
    void shouldExposeNotFoundFromTheUpdateUseCase() throws Exception {
        authenticate(USER_ID);
        when(update.execute(any(UserUpdateInputDTO.class), eq(USER_ID))).thenThrow(new UserNotFoundException(USER_ID));

        mvc.perform(put("/api/v1/user/{id}", USER_ID).contentType(MediaType.APPLICATION_JSON).content(USER_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteTheAuthenticatedUsersOwnAccountWith204() throws Exception {
        authenticate(USER_ID);

        mvc.perform(delete("/api/v1/user/{id}", USER_ID))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(delete).execute(USER_ID);
    }

    @Test
    void shouldRejectDeletingAnotherAccountBeforeCallingTheUseCase() throws Exception {
        authenticate(8L);

        mvc.perform(delete("/api/v1/user/{id}", USER_ID))
                .andExpect(status().isForbidden());

        verifyNoInteractions(delete);
    }

    @Test
    void shouldReadTheGrantTypeDtoFromTheJsonBodyForTheOwnAccount() throws Exception {
        authenticate(USER_ID);
        when(grantType.execute(new GrantUserTypeInputDTO(USER_ID, 3))).thenReturn(output());

        mvc.perform(patch("/api/v1/user/grant-level").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userID\":7,\"userTypeID\":3}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.type.title").value("Cliente"));

        verify(grantType).execute(new GrantUserTypeInputDTO(USER_ID, 3));
    }

    @Test
    void shouldRejectGrantingATypeToAnotherAccount() throws Exception {
        authenticate(8L);

        mvc.perform(patch("/api/v1/user/grant-level").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userID\":7,\"userTypeID\":3}"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(grantType);
    }

    @Test
    void shouldChangeTheOwnPasswordWhenTheUseCaseSucceeds() throws Exception {
        authenticateForPasswordChange();
        when(changePassword.changePassword(any(UserChangePasswordInputDTO.class))).thenReturn(true);

        mvc.perform(patch("/api/v1/user/change-password/{id}", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON).content(PASSWORD_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Senha alterada"));

        verify(changePassword).changePassword(new UserChangePasswordInputDTO(LOGIN, "NovaSenha@123"));
    }

    @Test
    void shouldReturn422InsteadOfSuccessWhenPasswordChangeReturnsFalse() throws Exception {
        authenticateForPasswordChange();
        when(changePassword.changePassword(any(UserChangePasswordInputDTO.class))).thenReturn(false);

        mvc.perform(patch("/api/v1/user/change-password/{id}", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON).content(PASSWORD_JSON))
                .andExpect(status().is(422))
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.success").doesNotExist());
    }

    @Test
    void shouldRejectChangingAnotherAccountsPasswordBeforeCallingTheUseCase() throws Exception {
        authenticate(8L);

        mvc.perform(patch("/api/v1/user/change-password/{id}", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON).content(PASSWORD_JSON))
                .andExpect(status().isForbidden());

        verifyNoInteractions(changePassword);
    }

    @Test
    void shouldRejectAPasswordChangeWithAnotherLogin() throws Exception {
        authenticateForPasswordChange();

        mvc.perform(patch("/api/v1/user/change-password/{id}", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"outra.conta\",\"password\":\"NovaSenha@123\"}"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(changePassword);
    }

    @Test
    void shouldValidateTheNewPasswordBeforeCallingTheUseCase() throws Exception {
        mvc.perform(patch("/api/v1/user/change-password/{id}", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"vinicius.qa\",\"password\":\"123\"}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(changePassword);
    }

    @Test
    void shouldExposeUnauthorizedFromThePasswordUseCase() throws Exception {
        authenticateForPasswordChange();
        when(changePassword.changePassword(any(UserChangePasswordInputDTO.class)))
                .thenThrow(new UnauthorizedException());

        mvc.perform(patch("/api/v1/user/change-password/{id}", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON).content(PASSWORD_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void shouldAuthenticateValidCredentials() throws Exception {
        when(validator.authUser(new UserAuthInputDTO(LOGIN, "Senha@123"))).thenReturn(true);

        mvc.perform(post("/api/v1/user/auth-login").contentType(MediaType.APPLICATION_JSON).content(AUTH_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Usuário autenticado"));

        verify(validator).authUser(new UserAuthInputDTO(LOGIN, "Senha@123"));
    }

    @Test
    void shouldReturn401InsteadOfSuccessWhenAuthenticationReturnsFalse() throws Exception {
        when(validator.authUser(any(UserAuthInputDTO.class))).thenReturn(false);

        mvc.perform(post("/api/v1/user/auth-login").contentType(MediaType.APPLICATION_JSON).content(AUTH_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.success").doesNotExist());
    }

    @Test
    void shouldExposeUnauthorizedFromTheAuthenticationValidator() throws Exception {
        when(validator.authUser(any(UserAuthInputDTO.class))).thenThrow(new UnauthorizedException());

        mvc.perform(post("/api/v1/user/auth-login").contentType(MediaType.APPLICATION_JSON).content(AUTH_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void shouldValidateAuthenticationFieldsBeforeCallingTheValidator() throws Exception {
        mvc.perform(post("/api/v1/user/auth-login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"abc\",\"password\":\"123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations.login").exists())
                .andExpect(jsonPath("$.violations.password").exists());

        verifyNoInteractions(validator);
    }
}
