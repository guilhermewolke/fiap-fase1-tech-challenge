package com.github.techChallenge.application.usecases.menuItem;

import com.github.techChallenge.application.exceptions.DuplicateMenuItemNameException;
import com.github.techChallenge.application.exceptions.MenuItemNotFoundException;
import com.github.techChallenge.application.gateways.menuItem.IMenuItemGateway;
import com.github.techChallenge.domain.menuItem.IMenuItemMapper;
import com.github.techChallenge.domain.menuItem.MenuItem;
import com.github.techChallenge.domain.menuItem.dto.MenuItemCreateInputDTO;
import com.github.techChallenge.domain.menuItem.dto.MenuItemOutputDTO;
import com.github.techChallenge.domain.menuItem.dto.MenuItemUpdateInputDTO;
import com.github.techChallenge.application.validators.RestaurantAccessPolicy;
import com.github.techChallenge.application.gateways.restaurant.IRestaurantGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuItemUseCasesTest {

    @Mock
    private IMenuItemGateway gateway;

    @Mock
    private IMenuItemMapper mapper;

    @Mock
    private RestaurantAccessPolicy access;

    @Mock
    private IRestaurantGateway restaurants;

    private final BigDecimal price = new BigDecimal("29.90");

    private final MenuItem saved = new MenuItem(1L, "X-Burger", "Pão e carne",
            price, false, "/fotos/x-burger.png", 7L);

    private final MenuItemOutputDTO output = new MenuItemOutputDTO(1L, "X-Burger", "Pão e carne",
            price, false, "/fotos/x-burger.png", 7L);

    @Test
    @DisplayName("Deve criar um item do cardápio")
    void shouldCreateMenuItem() {
        // Arrange
        MenuItemCreateInputDTO dto = new MenuItemCreateInputDTO(" X-Burger ", "Pão e carne",
                price, false, "/fotos/x-burger.png");

        when(gateway.nameExists("X-Burger", 7L)).thenReturn(false);
        when(gateway.create(any(MenuItem.class))).thenReturn(saved);
        when(mapper.fromDomainToOutputDTO(saved)).thenReturn(output);

        // Act
        MenuItemOutputDTO result = new CreateMenuItemUseCase(gateway, mapper, access).execute(dto, 7L, 1L);

        // Assert
        ArgumentCaptor<MenuItem> captor = ArgumentCaptor.forClass(MenuItem.class);
        verify(gateway).create(captor.capture());
        assertNull(captor.getValue().getId());
        assertEquals("X-Burger", captor.getValue().getName());
        assertEquals(7L, captor.getValue().getRestaurantId());
        assertEquals(output, result);
    }

    @Test
    @DisplayName("Não deve criar item com nome repetido no mesmo restaurante")
    void shouldNotCreateDuplicatedName() {
        // Arrange
        MenuItemCreateInputDTO dto = new MenuItemCreateInputDTO("X-Burger", "Pão e carne",
                price, false, "/fotos/x-burger.png");

        when(gateway.nameExists("X-Burger", 7L)).thenReturn(true);

        // Act + Assert
        assertThrows(DuplicateMenuItemNameException.class,
                () -> new CreateMenuItemUseCase(gateway, mapper, access).execute(dto, 7L, 1L));
        verify(gateway, never()).create(any());
    }

    @Test
    @DisplayName("Deve atualizar um item do cardápio")
    void shouldUpdateMenuItem() {
        // Arrange
        MenuItemUpdateInputDTO dto = new MenuItemUpdateInputDTO("X-Salada", "Pão, carne e salada",
                new BigDecimal("32.00"), true, "/fotos/x-salada.png");

        when(gateway.existById(1L)).thenReturn(true);
        when(gateway.find(1L)).thenReturn(saved);
        when(gateway.nameExists("X-Salada", 7L, 1L)).thenReturn(false);
        when(gateway.update(saved, 1L)).thenReturn(saved);
        when(mapper.fromDomainToOutputDTO(saved)).thenReturn(output);

        // Act
        MenuItemOutputDTO result = new UpdateMenuItemUseCase(gateway, mapper, access).execute(dto, 7L, 1L, 1L);

        // Assert
        assertEquals("X-Salada", saved.getName());
        assertEquals(new BigDecimal("32.00"), saved.getPrice());
        assertTrue(saved.isDineInOnly());
        assertEquals(7L, saved.getRestaurantId());
        assertEquals(output, result);
    }

    @Test
    @DisplayName("Não deve atualizar item inexistente")
    void shouldNotUpdateMissingMenuItem() {
        // Arrange
        MenuItemUpdateInputDTO dto = new MenuItemUpdateInputDTO("X-Salada", null,
                price, false, null);

        when(gateway.existById(99L)).thenReturn(false);

        // Act + Assert
        assertThrows(MenuItemNotFoundException.class,
                () -> new UpdateMenuItemUseCase(gateway, mapper, access).execute(dto, 7L, 99L, 1L));
        verify(gateway, never()).update(any(), anyLong());
    }

    @Test
    @DisplayName("Não deve atualizar para um nome já usado por outro item do restaurante")
    void shouldNotUpdateToDuplicatedName() {
        // Arrange
        MenuItemUpdateInputDTO dto = new MenuItemUpdateInputDTO("X-Salada", "Pão e carne",
                price, false, "/fotos/x-salada.png");

        when(gateway.existById(1L)).thenReturn(true);
        when(gateway.find(1L)).thenReturn(saved);
        when(gateway.nameExists("X-Salada", 7L, 1L)).thenReturn(true);

        // Act + Assert
        assertThrows(DuplicateMenuItemNameException.class,
                () -> new UpdateMenuItemUseCase(gateway, mapper, access).execute(dto, 7L, 1L, 1L));
        verify(gateway, never()).update(any(), anyLong());
    }

    @Test
    @DisplayName("Deve buscar um item pelo id")
    void shouldFindMenuItem() {
        // Arrange
        when(gateway.existById(1L)).thenReturn(true);
        when(gateway.find(1L)).thenReturn(saved);
        when(mapper.fromDomainToOutputDTO(saved)).thenReturn(output);

        // Act
        MenuItemOutputDTO result = new FindMenuItemUseCase(gateway, mapper).execute(7L, 1L);

        // Assert
        assertEquals(output, result);
    }

    @Test
    @DisplayName("Deve falhar ao buscar item inexistente")
    void shouldNotFindMissingMenuItem() {
        // Arrange
        when(gateway.existById(99L)).thenReturn(false);

        // Act + Assert
        assertThrows(MenuItemNotFoundException.class,
                () -> new FindMenuItemUseCase(gateway, mapper).execute(7L, 99L));
        verify(gateway, never()).find(anyLong());
    }

    @Test
    @DisplayName("Deve listar os itens de um restaurante")
    void shouldListMenuItemsByRestaurant() {
        // Arrange
        Page<MenuItem> domainPage = new PageImpl<>(List.of(saved));
        Page<MenuItemOutputDTO> outputPage = new PageImpl<>(List.of(output));

        when(gateway.listByRestaurant(7L, 0, 10)).thenReturn(domainPage);
        when(mapper.fromDomainPageToOutputDTOPage(domainPage)).thenReturn(outputPage);

        // Act
        Page<MenuItemOutputDTO> result = new ListMenuItemUseCase(gateway, mapper, restaurants).execute(7L, 0, 10);

        // Assert
        assertEquals(outputPage, result);
    }

    @Test
    @DisplayName("Deve excluir um item existente")
    void shouldDeleteMenuItem() {
        // Arrange
        when(gateway.existById(1L)).thenReturn(true);
        when(gateway.find(1L)).thenReturn(saved);

        // Act
        new DeleteMenuItemUseCase(gateway, mapper, access).execute(7L, 1L, 1L);

        // Assert
        verify(gateway).delete(1L);
    }

    @Test
    @DisplayName("Não deve excluir item inexistente")
    void shouldNotDeleteMissingMenuItem() {
        // Arrange
        when(gateway.existById(99L)).thenReturn(false);

        // Act + Assert
        assertThrows(MenuItemNotFoundException.class,
                () -> new DeleteMenuItemUseCase(gateway, mapper, access).execute(7L, 99L, 1L));
        verify(gateway, never()).delete(anyLong());
    }
}

