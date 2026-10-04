package com.github.techChallenge.infrastructure.repositories.restaurantCategory;

import com.github.techChallenge.application.exceptions.RestaurantCategoryNotFoundException;
import com.github.techChallenge.application.repositories.IRestaurantCategoryRepository;
import com.github.techChallenge.domain.restaurantCategory.IRestaurantCategoryMapper;
import com.github.techChallenge.domain.restaurantCategory.RestaurantCategory;
import com.github.techChallenge.infrastructure.entities.restaurantCategory.RestaurantCategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class RestaurantCategoryRepositoryGateway implements IRestaurantCategoryRepository {

    @Autowired
    private RestaurantCategoryRepository repository;
    @Autowired
    private IRestaurantCategoryMapper mapper;

    @Override
    public RestaurantCategory create(RestaurantCategory restaurantCategory) {
        RestaurantCategoryEntity restaurantCategoryEntity = new RestaurantCategoryEntity(restaurantCategory);
        restaurantCategoryEntity = this.repository.save(restaurantCategoryEntity);
        return this.mapper.fromEntityToDomain(restaurantCategoryEntity);
    }

    @Override
    public RestaurantCategory update(RestaurantCategory restaurantCategory, int id) {

        Optional<RestaurantCategoryEntity> entity = this.repository.findById(id);
        if (!entity.isPresent()) throw new RestaurantCategoryNotFoundException("Nível de usuário não encontrado");

        RestaurantCategoryEntity restaurantCategoryEntity = entity.get();
        restaurantCategoryEntity.setTitle(restaurantCategory.getTitle());

        restaurantCategoryEntity = this.repository.save(restaurantCategoryEntity);
        return this.mapper.fromEntityToDomain(restaurantCategoryEntity);
    }

    @Override
    public RestaurantCategory findByID(int id) {
        Optional<RestaurantCategoryEntity> entity = this.repository.findById(id);
        if (!entity.isPresent()) throw new RestaurantCategoryNotFoundException("Nível de usuário não encontrado");

        return this.mapper.fromEntityToDomain(entity.get());
    }

    @Override
    public Page<RestaurantCategory> list(int page, int offset) {
        Pageable pageable = PageRequest.of(page, offset);
        Page<RestaurantCategoryEntity> entitiesPage =  this.repository.findAll(pageable);

        return this.mapper.fromEntityPageToDomainPage(entitiesPage);
    }

    @Override
    public void delete(int id) {
        Optional<RestaurantCategoryEntity> entity = this.repository.findById(id);
        if (!entity.isPresent()) throw new RestaurantCategoryNotFoundException("Nível de usuário não encontrado");

        this.repository.delete(entity.get());
    }

    @Override
    public boolean existsByTitle(String title) {
        return repository.existsByTitleIgnoreCase(title);
    }

    @Override
    public boolean existsByTitleAndIdNot(String title, int id) {
        return this.repository.existsByTitleAndIdNot(title, id);
    }

    @Override
    public boolean existsById(int id) {
        return this.repository.existsById(id);
    }
}
