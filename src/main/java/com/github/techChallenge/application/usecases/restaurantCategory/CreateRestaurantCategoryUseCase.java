package com.github.techChallenge.application.usecases.restaurantCategory;

import com.github.techChallenge.application.exceptions.DuplicateRestaurantCategoryTitleException;
import com.github.techChallenge.application.gateways.restaurantCategory.IRestaurantCategoryGateway;
import com.github.techChallenge.domain.restaurantCategory.IRestaurantCategoryMapper;
import com.github.techChallenge.domain.restaurantCategory.RestaurantCategory;
import com.github.techChallenge.domain.restaurantCategory.dto.RestaurantCategoryCreateInputDTO;
import com.github.techChallenge.domain.restaurantCategory.dto.RestaurantCategoryOutputDTO;

public class CreateRestaurantCategoryUseCase extends RestaurantCategoryUseCase {

    public CreateRestaurantCategoryUseCase(IRestaurantCategoryGateway gateway, IRestaurantCategoryMapper mapper) {
        super(gateway, mapper);
    }

    public RestaurantCategoryOutputDTO execute(RestaurantCategoryCreateInputDTO dto) {

        if (gateway.titleExists(dto.title()))
            throw new DuplicateRestaurantCategoryTitleException(dto.title());

        RestaurantCategory restaurantCategory = RestaurantCategory.create(dto.title());

        restaurantCategory = this.gateway.create(restaurantCategory);
        return this.mapper.fromDomainToOutputDTO(restaurantCategory);
    }
}
