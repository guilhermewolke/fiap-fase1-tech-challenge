package com.github.techChallenge.application.gateways.menuItem;

import com.github.techChallenge.application.exceptions.MenuItemNotFoundException;
import com.github.techChallenge.application.repositories.IMenuItemRepository;
import com.github.techChallenge.domain.menuItem.MenuItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuItemGatewayTest {
    @Mock private IMenuItemRepository repository;
    private MenuItemGateway gateway;

    @BeforeEach
    void setUp() {
        gateway = new MenuItemGateway(repository);
    }

    private MenuItem item(String name) {
        return MenuItem.create(name, "Descrição do prato", new BigDecimal("29.90"), false, "/fotos/prato.png", 12L);
    }

    @Test
    void shouldReturnThePersistedItemWhenCreating() {
        MenuItem input = item("Entrada");
        MenuItem persisted = item("Entrada persistida");
        when(repository.create(input)).thenReturn(persisted);

        assertSame(persisted, gateway.create(input));

        verify(repository).create(input);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldUpdateTheRequestedIdentityAndReturnTheRepositoryResult() {
        MenuItem input = item("Prato atualizado");
        MenuItem persisted = item("Prato salvo");
        when(repository.update(input, 31L)).thenReturn(persisted);

        assertSame(persisted, gateway.update(input, 31L));

        verify(repository).update(input, 31L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldFindTheRequestedItem() {
        MenuItem persisted = item("Prato");
        when(repository.findByID(31L)).thenReturn(persisted);

        assertSame(persisted, gateway.find(31L));

        verify(repository).findByID(31L);
    }

    @Test
    void shouldPreserveTheNotFoundException() {
        var failure = new MenuItemNotFoundException(99L);
        when(repository.findByID(99L)).thenThrow(failure);

        assertSame(failure, assertThrows(MenuItemNotFoundException.class, () -> gateway.find(99L)));
    }

    @Test
    void shouldScopeThePageToTheRequestedRestaurant() {
        Page<MenuItem> persisted = new PageImpl<>(List.of(item("Prato")), PageRequest.of(3, 4), 17);
        when(repository.listByRestaurant(12L, 3, 4)).thenReturn(persisted);

        Page<MenuItem> result = gateway.listByRestaurant(12L, 3, 4);

        assertSame(persisted, result);
        assertEquals(3, result.getNumber());
        assertEquals(4, result.getSize());
        assertEquals(17, result.getTotalElements());
        verify(repository).listByRestaurant(12L, 3, 4);
    }

    @Test
    void shouldDeleteOnlyTheRequestedItem() {
        gateway.delete(31L);

        verify(repository).delete(31L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldPreserveDeleteFailures() {
        var failure = new MenuItemNotFoundException(99L);
        doThrow(failure).when(repository).delete(99L);

        assertSame(failure, assertThrows(MenuItemNotFoundException.class, () -> gateway.delete(99L)));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void shouldCheckNameUniquenessInsideTheSpecifiedRestaurant(boolean exists) {
        when(repository.existsByNameAndRestaurantId("Prato", 12L)).thenReturn(exists);

        assertEquals(exists, gateway.nameExists("Prato", 12L));

        verify(repository).existsByNameAndRestaurantId("Prato", 12L);
        verifyNoMoreInteractions(repository);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void shouldExcludeTheCurrentItemWhenCheckingDuplicateNames(boolean exists) {
        when(repository.existsByNameAndRestaurantIdAndIdNot("Prato", 12L, 31L)).thenReturn(exists);

        assertEquals(exists, gateway.nameExists("Prato", 12L, 31L));

        verify(repository).existsByNameAndRestaurantIdAndIdNot("Prato", 12L, 31L);
        verifyNoMoreInteractions(repository);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void shouldReportWhetherTheItemExists(boolean exists) {
        when(repository.existsById(31L)).thenReturn(exists);

        assertEquals(exists, gateway.existById(31L));

        verify(repository).existsById(31L);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void shouldReportWhetherTheRestaurantHasAnyMenuItems(boolean exists) {
        when(repository.existsByRestaurantId(12L)).thenReturn(exists);

        assertEquals(exists, gateway.existsByRestaurant(12L));

        verify(repository).existsByRestaurantId(12L);
    }
}
