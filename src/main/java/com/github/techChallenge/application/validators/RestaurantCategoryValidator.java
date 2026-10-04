package com.github.techChallenge.application.validators;

import com.github.techChallenge.application.gateways.restaurantCategory.IRestaurantCategoryGateway;
import com.github.techChallenge.application.usecases.restaurantCategory.RestaurantCategoryUseCase;
import com.github.techChallenge.domain.restaurantCategory.IRestaurantCategoryMapper;

public class RestaurantCategoryValidator extends RestaurantCategoryUseCase {

    public RestaurantCategoryValidator(IRestaurantCategoryGateway gateway, IRestaurantCategoryMapper mapper){
        super(gateway, mapper);
    }

    public boolean titleExists(String title, int id) {
        return this.gateway.titleExists(title, id);
    }

    public boolean titleExists(String title) {
        return this.gateway.titleExists(title);
    }

    public boolean existsByID(int id) {return this.gateway.existById(id);}

}
