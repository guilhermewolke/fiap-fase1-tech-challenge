package com.github.techChallenge.application.repositories;

import com.github.techChallenge.domain.restaurantCategory.RestaurantCategory;
import org.springframework.data.domain.Page;

public interface IRestaurantCategoryRepository {
    RestaurantCategory create(RestaurantCategory restaurantCategory);
    RestaurantCategory update(RestaurantCategory restaurantCategory, int id);
    RestaurantCategory findByID(int id);
    Page<RestaurantCategory> list(int page, int offset);
    void delete(int id);
    boolean existsByTitle(String title);
    boolean existsByTitleAndIdNot(String title, int id);
    boolean existsById(int id);
}