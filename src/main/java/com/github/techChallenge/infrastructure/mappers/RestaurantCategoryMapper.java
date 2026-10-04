package com.github.techChallenge.infrastructure.mappers;

import com.github.techChallenge.domain.restaurantCategory.IRestaurantCategoryMapper;
import com.github.techChallenge.domain.restaurantCategory.RestaurantCategory;
import com.github.techChallenge.domain.restaurantCategory.dto.RestaurantCategoryOutputDTO;
import com.github.techChallenge.infrastructure.entities.restaurantCategory.RestaurantCategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RestaurantCategoryMapper implements IRestaurantCategoryMapper {
    @Override
    public RestaurantCategory fromEntityToDomain(RestaurantCategoryEntity restaurantCategoryEntity) {
        if (Optional.ofNullable(restaurantCategoryEntity).isPresent()) {
            return new RestaurantCategory(
                    restaurantCategoryEntity.getId(),
                    restaurantCategoryEntity.getTitle());
        }
        return null;
    }

    @Override
    public RestaurantCategoryOutputDTO fromDomainToOutputDTO(RestaurantCategory restaurantCategory) {
        if (Optional.ofNullable(restaurantCategory).isPresent()) {
            return new RestaurantCategoryOutputDTO(
                    restaurantCategory.getId(),
                    restaurantCategory.getTitle());
        }

        return null;
    }

    @Override
    public RestaurantCategoryEntity fromDomainToEntity(RestaurantCategory domain) {
        if (Optional.ofNullable(domain).isPresent()) {
            return new RestaurantCategoryEntity(domain);
        }
        return null;
    }

    @Override
    public Page<RestaurantCategory> fromEntityPageToDomainPage(Page<RestaurantCategoryEntity> entitiesPage) {
        return entitiesPage.map(u -> {
            return new RestaurantCategory(
                u.getId(),
                u.getTitle());
        });
    }

    @Override
    public Page<RestaurantCategoryOutputDTO> fromDomainPageToOutputDTOPage(Page<RestaurantCategory> domainPage) {
        return domainPage.map(u -> {
            return new RestaurantCategoryOutputDTO(
                u.getId(),
                u.getTitle());
        });
    }

}
