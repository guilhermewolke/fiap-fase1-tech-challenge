package com.github.techChallenge.application.usecases.menuItem;

import com.github.techChallenge.application.exceptions.DuplicateMenuItemNameException;
import com.github.techChallenge.application.gateways.menuItem.IMenuItemGateway;
import com.github.techChallenge.domain.menuItem.IMenuItemMapper;
import com.github.techChallenge.domain.menuItem.MenuItem;
import com.github.techChallenge.domain.menuItem.dto.MenuItemOutputDTO;
import com.github.techChallenge.domain.menuItem.dto.MenuItemUpdateInputDTO;
import com.github.techChallenge.application.validators.RestaurantAccessPolicy;
import org.springframework.transaction.annotation.Transactional;

public class UpdateMenuItemUseCase extends MenuItemUseCase {
    private final RestaurantAccessPolicy access;

    public UpdateMenuItemUseCase(IMenuItemGateway gateway, IMenuItemMapper mapper, RestaurantAccessPolicy access) {
        super(gateway, mapper);
        this.access = access;
    }

    @Transactional
    public MenuItemOutputDTO execute(MenuItemUpdateInputDTO dto, Long restaurantId, Long id, Long authenticatedUserId) {
        MenuItem menuItem = findInRestaurant(restaurantId, id);
        access.requireOwner(restaurantId, authenticatedUserId);
        menuItem.update(
                dto.name(),
                dto.description(),
                dto.price(),
                dto.dineInOnly(),
                dto.photoPath()
        );

        if (this.gateway.nameExists(menuItem.getName(), menuItem.getRestaurantId(), id))
            throw new DuplicateMenuItemNameException(menuItem.getName());

        menuItem = this.gateway.update(menuItem, id);
        return this.mapper.fromDomainToOutputDTO(menuItem);
    }
}

