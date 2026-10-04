package com.github.techChallenge.application.usecases.restaurantCategory;

import com.github.techChallenge.application.gateways.restaurantCategory.IRestaurantCategoryGateway;
import com.github.techChallenge.domain.restaurantCategory.IRestaurantCategoryMapper;
import com.github.techChallenge.domain.restaurantCategory.RestaurantCategory;
import com.github.techChallenge.domain.restaurantCategory.dto.RestaurantCategoryOutputDTO;

public class FindRestaurantCategoryUseCase extends RestaurantCategoryUseCase {

    public FindRestaurantCategoryUseCase(IRestaurantCategoryGateway gateway, IRestaurantCategoryMapper mapper) {
        super(gateway, mapper);
    }

    public RestaurantCategoryOutputDTO execute(int id) {
        RestaurantCategory restaurantCategory = this.gateway.find(id);
        return this.mapper.fromDomainToOutputDTO(restaurantCategory);
    }
}
