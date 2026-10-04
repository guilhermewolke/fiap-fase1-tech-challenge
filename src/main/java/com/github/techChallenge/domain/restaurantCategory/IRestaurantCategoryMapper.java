package com.github.techChallenge.domain.restaurantCategory;

import com.github.techChallenge.domain.restaurantCategory.dto.RestaurantCategoryOutputDTO;
import com.github.techChallenge.infrastructure.entities.restaurantCategory.RestaurantCategoryEntity;
import org.springframework.data.domain.Page;

public interface IRestaurantCategoryMapper {
    RestaurantCategory fromEntityToDomain(RestaurantCategoryEntity restaurantCategoryEntity);
    RestaurantCategoryOutputDTO fromDomainToOutputDTO(RestaurantCategory domain);
    RestaurantCategoryEntity fromDomainToEntity(RestaurantCategory domain);
    Page<RestaurantCategory> fromEntityPageToDomainPage(Page<RestaurantCategoryEntity> entitiesPage);
    Page<RestaurantCategoryOutputDTO> fromDomainPageToOutputDTOPage(Page<RestaurantCategory> domainPage);
}
