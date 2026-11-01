package com.github.techChallenge.application.usecases.menuItem;

import com.github.techChallenge.application.exceptions.DuplicateMenuItemNameException;
import com.github.techChallenge.application.gateways.menuItem.IMenuItemGateway;
import com.github.techChallenge.domain.menuItem.IMenuItemMapper;
import com.github.techChallenge.domain.menuItem.MenuItem;
import com.github.techChallenge.domain.menuItem.dto.MenuItemCreateInputDTO;
import com.github.techChallenge.domain.menuItem.dto.MenuItemOutputDTO;
import com.github.techChallenge.application.validators.RestaurantAccessPolicy;
import org.springframework.transaction.annotation.Transactional;

public class CreateMenuItemUseCase extends MenuItemUseCase {
    private final RestaurantAccessPolicy access;

    public CreateMenuItemUseCase(IMenuItemGateway gateway, IMenuItemMapper mapper, RestaurantAccessPolicy access) {
        super(gateway, mapper);
        this.access = access;
    }

    @Transactional
    public MenuItemOutputDTO execute(MenuItemCreateInputDTO dto, Long restaurantId, Long authenticatedUserId) {
        MenuItem menuItem = MenuItem.create(
                dto.name(),
                dto.description(),
                dto.price(),
                dto.dineInOnly(),
                dto.photoPath(),
                restaurantId
        );

        access.requireOwner(menuItem.getRestaurantId(), authenticatedUserId);

        if (this.gateway.nameExists(menuItem.getName(), menuItem.getRestaurantId()))
            throw new DuplicateMenuItemNameException(menuItem.getName());

        menuItem = this.gateway.create(menuItem);
        return this.mapper.fromDomainToOutputDTO(menuItem);
    }
}

