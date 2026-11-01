package com.github.techChallenge.application.usecases.menuItem;

import com.github.techChallenge.application.exceptions.MenuItemNotFoundException;
import com.github.techChallenge.application.gateways.menuItem.IMenuItemGateway;
import com.github.techChallenge.domain.menuItem.IMenuItemMapper;
import com.github.techChallenge.domain.menuItem.MenuItem;

public class MenuItemUseCase {
    protected final IMenuItemGateway gateway;
    protected final IMenuItemMapper mapper;

    public MenuItemUseCase(IMenuItemGateway gateway, IMenuItemMapper mapper) {
        this.gateway = gateway;
        this.mapper = mapper;
    }

    protected MenuItem findInRestaurant(Long restaurantId, Long id) {
        if (!gateway.existById(id)) throw new MenuItemNotFoundException(id);

        MenuItem item = gateway.find(id);
        if (!item.getRestaurantId().equals(restaurantId)) {
            throw new MenuItemNotFoundException(
                    "Item do cardápio não encontrado para o id " + id + " no restaurante " + restaurantId);
        }
        return item;
    }
}
