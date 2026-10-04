package com.github.techChallenge.infrastructure.config;

import com.github.techChallenge.application.gateways.restaurantCategory.IRestaurantCategoryGateway;
import com.github.techChallenge.application.gateways.restaurantCategory.RestaurantCategoryGateway;
import com.github.techChallenge.application.usecases.restaurantCategory.*;
import com.github.techChallenge.application.validators.RestaurantCategoryValidator;
import com.github.techChallenge.domain.restaurantCategory.IRestaurantCategoryMapper;
import com.github.techChallenge.infrastructure.mappers.RestaurantCategoryMapper;
import com.github.techChallenge.infrastructure.repositories.restaurantCategory.RestaurantCategoryRepositoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestaurantCategoryConfig {

    @Bean
    CreateRestaurantCategoryUseCase createRestaurantCategoryUseCase(IRestaurantCategoryGateway gateway, IRestaurantCategoryMapper mapper) {
        return new CreateRestaurantCategoryUseCase(gateway, mapper);
    }

    @Bean
    UpdateRestaurantCategoryUseCase updateRestaurantCategoryUseCase(IRestaurantCategoryGateway gateway, RestaurantCategoryMapper mapper, RestaurantCategoryValidator restaurantCategoryValidator) {
        return new UpdateRestaurantCategoryUseCase(gateway, mapper, restaurantCategoryValidator);
    }

    @Bean
    FindRestaurantCategoryUseCase findRestaurantCategoryUseCase(IRestaurantCategoryGateway gateway, RestaurantCategoryMapper mapper) {
        return new FindRestaurantCategoryUseCase(gateway, mapper);
    }

    @Bean
    ListRestaurantCategoryUseCase listRestaurantCategoryUseCase(IRestaurantCategoryGateway gateway, RestaurantCategoryMapper mapper) {
        return new ListRestaurantCategoryUseCase(gateway, mapper);
    }

    @Bean
    DeleteRestaurantCategoryUseCase deleteRestaurantCategoryUseCase(IRestaurantCategoryGateway gateway, RestaurantCategoryMapper mapper) {
        return new DeleteRestaurantCategoryUseCase(gateway, mapper);
    }

    @Bean
    RestaurantCategoryValidator restaurantCategoryValidator(IRestaurantCategoryGateway gateway, IRestaurantCategoryMapper mapper) {
        return new RestaurantCategoryValidator(gateway, mapper);
    }

    @Bean
    RestaurantCategoryGateway restaurantCategoryGateway(RestaurantCategoryRepositoryGateway restaurantCategoryRepositoryGateway, RestaurantCategoryMapper restaurantCategoryMapper) {
        return new RestaurantCategoryGateway(restaurantCategoryRepositoryGateway, restaurantCategoryMapper);
    }

    @Bean
    RestaurantCategoryRepositoryGateway restaurantCategoryRepositoryGateway() {
        return new RestaurantCategoryRepositoryGateway();
    }

}
