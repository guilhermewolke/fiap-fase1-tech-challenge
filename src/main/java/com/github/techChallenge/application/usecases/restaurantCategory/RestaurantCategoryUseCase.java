package com.github.techChallenge.application.usecases.restaurantCategory;

import com.github.techChallenge.application.gateways.restaurantCategory.IRestaurantCategoryGateway;
import com.github.techChallenge.domain.restaurantCategory.IRestaurantCategoryMapper;

public class RestaurantCategoryUseCase {
    protected final IRestaurantCategoryGateway gateway;
    protected final IRestaurantCategoryMapper mapper;

    public RestaurantCategoryUseCase(IRestaurantCategoryGateway gateway, IRestaurantCategoryMapper mapper) {
        this.gateway = gateway;
        this.mapper = mapper;
    }

}
