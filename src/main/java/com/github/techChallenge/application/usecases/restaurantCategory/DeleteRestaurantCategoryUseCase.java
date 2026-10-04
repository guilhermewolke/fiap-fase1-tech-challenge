package com.github.techChallenge.application.usecases.restaurantCategory;

import com.github.techChallenge.application.gateways.restaurantCategory.IRestaurantCategoryGateway;
import com.github.techChallenge.domain.restaurantCategory.IRestaurantCategoryMapper;

public class DeleteRestaurantCategoryUseCase extends RestaurantCategoryUseCase {

    public DeleteRestaurantCategoryUseCase(IRestaurantCategoryGateway gateway, IRestaurantCategoryMapper mapper) {
        super(gateway, mapper);
    }

    public void execute(int id) {
        this.gateway.delete(id);
    }
}
