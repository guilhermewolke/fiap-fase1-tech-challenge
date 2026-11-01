package com.github.techChallenge.application.usecases.menuItem;

import com.github.techChallenge.application.exceptions.*;
import com.github.techChallenge.application.gateways.menuItem.IMenuItemGateway;
import com.github.techChallenge.application.gateways.restaurant.IRestaurantGateway;
import com.github.techChallenge.application.validators.RestaurantAccessPolicy;
import com.github.techChallenge.domain.menuItem.*;
import com.github.techChallenge.domain.menuItem.dto.*;
import com.github.techChallenge.domain.validation.DomainValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuItemAuthorizationTest {
    @Mock IMenuItemGateway gateway;
    @Mock IMenuItemMapper mapper;
    @Mock RestaurantAccessPolicy access;
    @Mock IRestaurantGateway restaurants;
    private MenuItemCreateInputDTO input() { return new MenuItemCreateInputDTO("Item", "Descrição", new BigDecimal("10.00"), false, "/foto"); }
    private MenuItemUpdateInputDTO update() { return new MenuItemUpdateInputDTO("Novo", "Nova descrição", new BigDecimal("12.00"), true, "/nova"); }
    private MenuItem item() { return new MenuItem(3L, "Item", "Descrição", new BigDecimal("10.00"), false, "/foto", 7L); }

    @Test void shouldCheckRestaurantOwnershipBeforeCreating() {
        new CreateMenuItemUseCase(gateway, mapper, access).execute(input(), 7L, 1L);
        verify(access).requireOwner(7L, 1L);
        verify(gateway).create(any());
    }

    @Test void shouldRejectAnotherOwnerWithoutPersisting() {
        doThrow(new ForbiddenOperationException("Outro proprietário")).when(access).requireOwner(7L, 2L);
        assertThrows(ForbiddenOperationException.class, () -> new CreateMenuItemUseCase(gateway, mapper, access).execute(input(), 7L, 2L));
        verifyNoInteractions(gateway, mapper);
    }

    @Test void shouldRejectUnknownRestaurantWithoutPersisting() {
        doThrow(new RestaurantNotFoundException(7L)).when(access).requireOwner(7L, 1L);
        assertThrows(RestaurantNotFoundException.class, () -> new CreateMenuItemUseCase(gateway, mapper, access).execute(input(), 7L, 1L));
        verifyNoInteractions(gateway, mapper);
    }

    @Test void shouldValidateNullAvailabilityBeforeCallingDependencies() {
        var dto = new MenuItemCreateInputDTO("Item", "Descrição", new BigDecimal("10.00"), null, "/foto");
        assertThrows(DomainValidationException.class, () -> new CreateMenuItemUseCase(gateway, mapper, access).execute(dto, 7L, 1L));
        verifyNoInteractions(gateway, mapper, access);
    }

    @Test void shouldCheckTheStoredRestaurantOnUpdate() {
        var item = item();
        when(gateway.existById(3L)).thenReturn(true);
        when(gateway.find(3L)).thenReturn(item);
        new UpdateMenuItemUseCase(gateway, mapper, access).execute(update(), 7L, 3L, 1L);
        verify(access).requireOwner(7L, 1L);
        verify(gateway).update(item, 3L);
        assertEquals(7L, item.getRestaurantId());
    }

    @Test void shouldRejectAnotherOwnerOnUpdateWithoutChangingTheItem() {
        var item = item();
        when(gateway.existById(3L)).thenReturn(true);
        when(gateway.find(3L)).thenReturn(item);
        doThrow(new ForbiddenOperationException("Outro proprietário")).when(access).requireOwner(7L, 2L);
        assertThrows(ForbiddenOperationException.class, () -> new UpdateMenuItemUseCase(gateway, mapper, access).execute(update(), 7L, 3L, 2L));
        assertEquals("Item", item.getName());
        verify(gateway, never()).update(any(), anyLong());
        verifyNoInteractions(mapper);
    }

    @Test void shouldRejectAnotherOwnerOnDelete() {
        when(gateway.existById(3L)).thenReturn(true);
        when(gateway.find(3L)).thenReturn(item());
        doThrow(new ForbiddenOperationException("Outro proprietário")).when(access).requireOwner(7L, 2L);
        assertThrows(ForbiddenOperationException.class, () -> new DeleteMenuItemUseCase(gateway, mapper, access).execute(7L, 3L, 2L));
        verify(gateway, never()).delete(anyLong());
    }

    @Test void shouldRejectListingTheMenuOfAnUnknownRestaurant() {
        when(restaurants.find(99L)).thenThrow(new RestaurantNotFoundException(99L));
        assertThrows(RestaurantNotFoundException.class, () -> new ListMenuItemUseCase(gateway, mapper, restaurants).execute(99L, 0, 10));
        verifyNoInteractions(gateway, mapper);
    }

    @Test void shouldNotFindAnItemInsideAnotherRestaurant() {
        when(gateway.existById(3L)).thenReturn(true);
        when(gateway.find(3L)).thenReturn(item());
        assertThrows(MenuItemNotFoundException.class,
                () -> new FindMenuItemUseCase(gateway, mapper).execute(8L, 3L));
        verifyNoInteractions(mapper);
    }

    @Test void shouldNotUpdateAnItemInsideAnotherRestaurantEvenForAnOwner() {
        var stored = item();
        when(gateway.existById(3L)).thenReturn(true);
        when(gateway.find(3L)).thenReturn(stored);
        assertThrows(MenuItemNotFoundException.class,
                () -> new UpdateMenuItemUseCase(gateway, mapper, access).execute(update(), 8L, 3L, 1L));
        assertEquals("Item", stored.getName());
        assertEquals(7L, stored.getRestaurantId());
        verify(gateway, never()).update(any(), anyLong());
        verifyNoInteractions(access, mapper);
    }

    @Test void shouldNotDeleteAnItemInsideAnotherRestaurantEvenForAnOwner() {
        when(gateway.existById(3L)).thenReturn(true);
        when(gateway.find(3L)).thenReturn(item());
        assertThrows(MenuItemNotFoundException.class,
                () -> new DeleteMenuItemUseCase(gateway, mapper, access).execute(8L, 3L, 1L));
        verify(gateway, never()).delete(anyLong());
        verifyNoInteractions(access, mapper);
    }

    @ParameterizedTest @CsvSource({"-1,10", "0,0", "0,101"})
    void shouldRejectInvalidPaginationBeforeConsultingAnyDatabase(int page, int offset) {
        assertThrows(DomainValidationException.class, () -> new ListMenuItemUseCase(gateway, mapper, restaurants).execute(7L, page, offset));
        verifyNoInteractions(gateway, mapper, restaurants);
    }
}
