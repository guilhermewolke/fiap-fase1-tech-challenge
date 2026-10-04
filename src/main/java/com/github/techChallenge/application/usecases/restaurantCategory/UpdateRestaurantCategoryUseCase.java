package com.github.techChallenge.application.usecases.restaurantCategory;

import com.github.techChallenge.application.exceptions.DuplicateRestaurantCategoryTitleException;
import com.github.techChallenge.application.exceptions.RestaurantCategoryNotFoundException;
import com.github.techChallenge.application.gateways.restaurantCategory.IRestaurantCategoryGateway;
import com.github.techChallenge.application.validators.RestaurantCategoryValidator;
import com.github.techChallenge.domain.restaurantCategory.IRestaurantCategoryMapper;
import com.github.techChallenge.domain.restaurantCategory.RestaurantCategory;
import com.github.techChallenge.domain.restaurantCategory.dto.RestaurantCategoryOutputDTO;
import com.github.techChallenge.domain.restaurantCategory.dto.RestaurantCategoryUpdateInputDTO;

public class UpdateRestaurantCategoryUseCase extends RestaurantCategoryUseCase {
    private final RestaurantCategoryValidator restaurantCategoryValidator;

    public UpdateRestaurantCategoryUseCase(IRestaurantCategoryGateway gateway, IRestaurantCategoryMapper mapper, RestaurantCategoryValidator restaurantCategoryValidator) {
        super(gateway, mapper);
        this.restaurantCategoryValidator = restaurantCategoryValidator;
    }

    public RestaurantCategoryOutputDTO execute(RestaurantCategoryUpdateInputDTO dto, int id) {
        if (!this.gateway.existById(id)) throw new RestaurantCategoryNotFoundException("Nível de usuário não encontrado");

        if (this.restaurantCategoryValidator.titleExists(dto.title(), id))
            throw new DuplicateRestaurantCategoryTitleException(dto.title());

        RestaurantCategory restaurantCategory = new RestaurantCategory(id, dto.title());

        restaurantCategory = this.gateway.update(restaurantCategory, id);
        return this.mapper.fromDomainToOutputDTO(restaurantCategory);
    }
}
