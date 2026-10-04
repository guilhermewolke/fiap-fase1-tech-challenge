package com.github.techChallenge.application.gateways.restaurantCategory;

import com.github.techChallenge.domain.restaurantCategory.RestaurantCategory;
import org.springframework.data.domain.Page;

public interface IRestaurantCategoryGateway {
    RestaurantCategory create(RestaurantCategory restaurantCategory);
    RestaurantCategory update(RestaurantCategory restaurantCategory, int id);
    RestaurantCategory find(int id);
    Page<RestaurantCategory> list(Integer page, Integer offset);
    void delete(int id);
    boolean titleExists(String title, int id);
    boolean titleExists(String title);
    boolean existById(int id);
}
