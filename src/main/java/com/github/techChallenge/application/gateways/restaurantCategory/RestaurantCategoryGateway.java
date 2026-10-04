package com.github.techChallenge.application.gateways.restaurantCategory;

import com.github.techChallenge.application.repositories.IRestaurantCategoryRepository;
import com.github.techChallenge.domain.restaurantCategory.IRestaurantCategoryMapper;
import com.github.techChallenge.domain.restaurantCategory.RestaurantCategory;
import org.springframework.data.domain.Page;

public class RestaurantCategoryGateway implements IRestaurantCategoryGateway {

    private final IRestaurantCategoryRepository repository;
    private final IRestaurantCategoryMapper mapper;

    public RestaurantCategoryGateway(IRestaurantCategoryRepository repository, IRestaurantCategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public RestaurantCategory create(RestaurantCategory category) {
        return repository.create(category);
    }

    @Override
    public RestaurantCategory update(RestaurantCategory category, int id) {
        category = this.repository.update(category, id);
        return category;
    }

    @Override
    public RestaurantCategory find(int id) {
        RestaurantCategory restaurantCategory = this.repository.findByID(id);
        return restaurantCategory;
    }

    @Override
    public Page<RestaurantCategory> list(Integer page, Integer offset) {
        Page<RestaurantCategory> categorys = this.repository.list(page, offset);

        return categorys;
    }

    @Override
    public void delete(int id) {
        this.repository.delete(id);
    }

    @Override
    public boolean titleExists(String title, int id) {
        return this.repository.existsByTitleAndIdNot(title, id);
    }

    @Override
    public boolean titleExists(String title) {
        return this.repository.existsByTitle(title);
    }

    @Override
    public boolean existById(int id) {
        return this.repository.existsById(id);
    }

}
