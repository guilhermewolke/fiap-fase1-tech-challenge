package com.github.techChallenge.application.usecases.restaurantCategory;

import com.github.techChallenge.application.gateways.restaurantCategory.IRestaurantCategoryGateway;
import com.github.techChallenge.domain.restaurantCategory.IRestaurantCategoryMapper;
import com.github.techChallenge.domain.restaurantCategory.RestaurantCategory;
import com.github.techChallenge.domain.restaurantCategory.dto.RestaurantCategoryOutputDTO;
import org.springframework.data.domain.Page;

public class ListRestaurantCategoryUseCase extends RestaurantCategoryUseCase {

    public ListRestaurantCategoryUseCase(IRestaurantCategoryGateway gateway, IRestaurantCategoryMapper mapper) {
        super(gateway, mapper);
    }

    public Page<RestaurantCategoryOutputDTO> execute(int page, int offset) {
        Page<RestaurantCategory> restaurantCategorys = this.gateway.list(page, offset);
        return this.mapper.fromDomainPageToOutputDTOPage(restaurantCategorys);
    }
}
