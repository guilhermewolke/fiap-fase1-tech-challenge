package com.github.techChallenge.application.usecases.menuItem;

import com.github.techChallenge.application.gateways.menuItem.IMenuItemGateway;
import com.github.techChallenge.domain.menuItem.IMenuItemMapper;
import com.github.techChallenge.domain.menuItem.MenuItem;
import com.github.techChallenge.domain.menuItem.dto.MenuItemOutputDTO;

public class FindMenuItemUseCase extends MenuItemUseCase {

    public FindMenuItemUseCase(IMenuItemGateway gateway, IMenuItemMapper mapper) {
        super(gateway, mapper);
    }

    public MenuItemOutputDTO execute(Long restaurantId, Long id) {
        MenuItem menuItem = findInRestaurant(restaurantId, id);
        return this.mapper.fromDomainToOutputDTO(menuItem);
    }
}
