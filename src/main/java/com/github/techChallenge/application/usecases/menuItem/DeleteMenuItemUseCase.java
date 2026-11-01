package com.github.techChallenge.application.usecases.menuItem;

import com.github.techChallenge.application.gateways.menuItem.IMenuItemGateway;
import com.github.techChallenge.domain.menuItem.IMenuItemMapper;
import com.github.techChallenge.application.validators.RestaurantAccessPolicy;
import org.springframework.transaction.annotation.Transactional;

public class DeleteMenuItemUseCase extends MenuItemUseCase {
    private final RestaurantAccessPolicy access;

    public DeleteMenuItemUseCase(IMenuItemGateway gateway, IMenuItemMapper mapper, RestaurantAccessPolicy access) {
        super(gateway, mapper);
        this.access = access;
    }

    @Transactional
    public void execute(Long restaurantId, Long id, Long authenticatedUserId) {
        findInRestaurant(restaurantId, id);
        access.requireOwner(restaurantId, authenticatedUserId);
        this.gateway.delete(id);
    }
}

