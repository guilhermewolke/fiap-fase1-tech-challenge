package com.github.techChallenge.application.usecases.usertype;

import com.github.techChallenge.application.exceptions.DuplicateUserTypeTitleException;
import com.github.techChallenge.application.exceptions.UserTypeNotFoundException;
import com.github.techChallenge.application.gateways.usertype.IUserTypeGateway;
import com.github.techChallenge.application.validators.UserTypeValidator;
import com.github.techChallenge.domain.usertype.UserType;
import com.github.techChallenge.domain.usertype.dto.UserTypeCreateInputDTO;
import com.github.techChallenge.domain.usertype.dto.UserTypeOutputDTO;
import com.github.techChallenge.domain.usertype.dto.UserTypeUpdateInputDTO;
import com.github.techChallenge.infrastructure.mappers.UserTypeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserTypeUseCasesTest {
    @Mock private IUserTypeGateway gateway;
    @Mock private UserTypeValidator validator;
    private CreateUserTypeUseCase create;
    private UpdateUserTypeUseCase update;
    private FindUserTypeUseCase find;
    private ListUserTypeUseCase list;
    private DeleteUserTypeUseCase delete;

    @BeforeEach
    void setUp() {
        var mapper = new UserTypeMapper();
        create = new CreateUserTypeUseCase(gateway, mapper, validator);
        update = new UpdateUserTypeUseCase(gateway, mapper, validator);
        find = new FindUserTypeUseCase(gateway, mapper);
        list = new ListUserTypeUseCase(gateway, mapper);
        delete = new DeleteUserTypeUseCase(gateway, mapper);
    }

    @Test
    void shouldCreateANewTypeAndReturnItsPersistedIdentity() {
        when(gateway.create(any(UserType.class))).thenReturn(new UserType(7, "Cliente"));

        UserTypeOutputDTO result = create.execute(new UserTypeCreateInputDTO("Cliente"));

        assertEquals(new UserTypeOutputDTO(7, "Cliente"), result);
        var input = ArgumentCaptor.forClass(UserType.class);
        verify(validator).titleExists("Cliente");
        verify(gateway).create(input.capture());
        assertEquals(0, input.getValue().getId());
        assertEquals("Cliente", input.getValue().getTitle());
    }

    @Test
    void shouldRejectDuplicateTitlesBeforeCreatingAnything() {
        when(validator.titleExists("Cliente")).thenReturn(true);

        assertThrows(DuplicateUserTypeTitleException.class,
                () -> create.execute(new UserTypeCreateInputDTO("Cliente")));

        verifyNoInteractions(gateway);
    }

    @Test
    void shouldPreserveCreateFailuresInsteadOfReturningAnOutput() {
        var failure = new IllegalStateException("Persistência indisponível");
        when(gateway.create(any(UserType.class))).thenThrow(failure);

        assertSame(failure, assertThrows(IllegalStateException.class,
                () -> create.execute(new UserTypeCreateInputDTO("Cliente"))));
    }

    @Test
    void shouldUpdateTheRequestedTypeAndExcludeItFromTheDuplicateCheck() {
        when(gateway.existById(7)).thenReturn(true);
        when(gateway.update(any(UserType.class), eq(7))).thenReturn(new UserType(7, "Fornecedor"));

        UserTypeOutputDTO result = update.execute(new UserTypeUpdateInputDTO("Fornecedor"), 7);

        assertEquals(new UserTypeOutputDTO(7, "Fornecedor"), result);
        var input = ArgumentCaptor.forClass(UserType.class);
        verify(validator).titleExists("Fornecedor", 7);
        verify(gateway).update(input.capture(), eq(7));
        assertEquals(7, input.getValue().getId());
        assertEquals("Fornecedor", input.getValue().getTitle());
    }

    @Test
    void shouldRejectAnUpdateOfAnUnknownTypeBeforeCheckingItsTitle() {
        assertThrows(UserTypeNotFoundException.class,
                () -> update.execute(new UserTypeUpdateInputDTO("Fornecedor"), 99));

        verify(gateway).existById(99);
        verifyNoMoreInteractions(gateway);
        verifyNoInteractions(validator);
    }

    @Test
    void shouldRejectAnUpdateToAnotherTypesTitleWithoutPersistingIt() {
        when(gateway.existById(7)).thenReturn(true);
        when(validator.titleExists("Administrador", 7)).thenReturn(true);

        assertThrows(DuplicateUserTypeTitleException.class,
                () -> update.execute(new UserTypeUpdateInputDTO("Administrador"), 7));

        verify(gateway, never()).update(any(UserType.class), anyInt());
    }

    @Test
    void shouldFindTheTypeAndMapItsIdentityAndTitle() {
        when(gateway.find(7)).thenReturn(new UserType(7, "Cliente"));

        assertEquals(new UserTypeOutputDTO(7, "Cliente"), find.execute(7));

        verify(gateway).find(7);
    }

    @Test
    void shouldPreserveNotFoundOnLookup() {
        var failure = new UserTypeNotFoundException("Tipo inexistente");
        when(gateway.find(99)).thenThrow(failure);

        assertSame(failure, assertThrows(UserTypeNotFoundException.class, () -> find.execute(99)));
    }

    @Test
    void shouldListTypesWithoutLosingOrderOrPagination() {
        var pageable = PageRequest.of(1, 2);
        when(gateway.list(1, 2)).thenReturn(new PageImpl<>(
                List.of(new UserType(3, "Dono"), new UserType(4, "Cliente")), pageable, 9));

        Page<UserTypeOutputDTO> result = list.execute(1, 2);

        assertEquals(List.of(new UserTypeOutputDTO(3, "Dono"), new UserTypeOutputDTO(4, "Cliente")), result.getContent());
        assertEquals(pageable, result.getPageable());
        assertEquals(9, result.getTotalElements());
        verify(gateway).list(1, 2);
    }

    @Test
    void shouldReturnAnEmptyPageWhenNoTypesAreRegistered() {
        var pageable = PageRequest.of(0, 5);
        when(gateway.list(0, 5)).thenReturn(Page.empty(pageable));

        Page<UserTypeOutputDTO> result = list.execute(0, 5);

        assertTrue(result.isEmpty());
        assertEquals(pageable, result.getPageable());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void shouldDeleteTheRequestedType() {
        delete.execute(7);

        verify(gateway).delete(7);
        verifyNoMoreInteractions(gateway);
    }

    @Test
    void shouldPreserveNotFoundOnDeletion() {
        var failure = new UserTypeNotFoundException("Tipo inexistente");
        doThrow(failure).when(gateway).delete(99);

        assertSame(failure, assertThrows(UserTypeNotFoundException.class, () -> delete.execute(99)));
    }
}
