package com.github.techChallenge.infrastructure.repositories.restaurantCategory;

import com.github.techChallenge.infrastructure.entities.restaurantCategory.RestaurantCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantCategoryRepository extends JpaRepository<RestaurantCategoryEntity, Integer> {

    boolean existsByTitleIgnoreCase(String title);
    boolean existsByTitleAndIdNot(String email, int id);

}