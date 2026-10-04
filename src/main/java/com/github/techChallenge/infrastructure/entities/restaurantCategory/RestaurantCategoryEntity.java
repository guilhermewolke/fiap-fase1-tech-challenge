package com.github.techChallenge.infrastructure.entities.restaurantCategory;

import com.github.techChallenge.domain.restaurantCategory.RestaurantCategory;
import jakarta.persistence.*;

@Entity
@Table(name="restaurante_categoria")
public class RestaurantCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "titulo", length = 50)
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {this.id = id;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RestaurantCategoryEntity() {
    }

    public RestaurantCategoryEntity(RestaurantCategory userLevel) {
        this.id = userLevel.getId();
        this.title = userLevel.getTitle();
    }
}
