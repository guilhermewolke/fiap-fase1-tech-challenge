package com.github.techChallenge.infrastructure.controllers.restaurantCategory;

import com.github.techChallenge.application.usecases.restaurantCategory.*;
import com.github.techChallenge.application.validators.RestaurantCategoryValidator;
import com.github.techChallenge.domain.restaurantCategory.dto.RestaurantCategoryCreateInputDTO;
import com.github.techChallenge.domain.restaurantCategory.dto.RestaurantCategoryOutputDTO;
import com.github.techChallenge.domain.restaurantCategory.dto.RestaurantCategoryUpdateInputDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/v1/restaurant-category", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantCategoryController implements IRestaurantCategoryController {

    private final CreateRestaurantCategoryUseCase createRestaurantCategoryUseCase;
    private final UpdateRestaurantCategoryUseCase updateRestaurantCategoryUseCase;
    private final FindRestaurantCategoryUseCase findRestaurantCategoryUseCase;
    private final ListRestaurantCategoryUseCase listRestaurantCategoryUseCase;
    private final DeleteRestaurantCategoryUseCase deleteRestaurantCategoryUseCase;
    private final RestaurantCategoryValidator restaurantCategoryValidator;

    public RestaurantCategoryController(CreateRestaurantCategoryUseCase createRestaurantCategoryUseCase,
                                        UpdateRestaurantCategoryUseCase updateRestaurantCategoryUseCase,
                                        FindRestaurantCategoryUseCase findRestaurantCategoryUseCase,
                                        ListRestaurantCategoryUseCase listRestaurantCategoryUseCase,
                                        DeleteRestaurantCategoryUseCase deleteRestaurantCategoryUseCase, RestaurantCategoryValidator restaurantCategoryValidator
    ) {
        this.createRestaurantCategoryUseCase = createRestaurantCategoryUseCase;
        this.updateRestaurantCategoryUseCase = updateRestaurantCategoryUseCase;
        this.findRestaurantCategoryUseCase = findRestaurantCategoryUseCase;
        this.listRestaurantCategoryUseCase = listRestaurantCategoryUseCase;
        this.deleteRestaurantCategoryUseCase = deleteRestaurantCategoryUseCase;
        this.restaurantCategoryValidator = restaurantCategoryValidator;
    }

    @PostMapping("/")
    @Override
    public ResponseEntity<RestaurantCategoryOutputDTO> create(
            @Valid @RequestBody RestaurantCategoryCreateInputDTO dto) {
        RestaurantCategoryOutputDTO response = this.createRestaurantCategoryUseCase.execute(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<RestaurantCategoryOutputDTO> update(
            @Valid @RequestBody RestaurantCategoryUpdateInputDTO dto,
            @PathVariable("id") int id
    ) {
        RestaurantCategoryOutputDTO response = this.updateRestaurantCategoryUseCase.execute(dto, id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<RestaurantCategoryOutputDTO> findByID(@PathVariable("id") int id) {
        RestaurantCategoryOutputDTO dto = this.findRestaurantCategoryUseCase.execute(id);

        return ResponseEntity.ok(dto);
    }

    @Override
    @GetMapping("/")
    public ResponseEntity<Page<RestaurantCategoryOutputDTO>> list(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "offset", defaultValue = "10") int offset) {
        Page<RestaurantCategoryOutputDTO> users = this.listRestaurantCategoryUseCase.execute(page, offset);

        return ResponseEntity.ok(users);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        this.deleteRestaurantCategoryUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

}
