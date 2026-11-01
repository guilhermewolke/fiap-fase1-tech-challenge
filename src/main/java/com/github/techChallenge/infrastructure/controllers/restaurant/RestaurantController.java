package com.github.techChallenge.infrastructure.controllers.restaurant;

import com.github.techChallenge.application.usecases.restaurant.*;
import com.github.techChallenge.domain.restaurant.dto.*;
import com.github.techChallenge.infrastructure.security.RestaurantUserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/restaurant")
public class RestaurantController implements IRestaurantController {
    private final CreateRestaurantUseCase create;
    private final UpdateRestaurantUseCase update;
    private final FindRestaurantUseCase find;
    private final ListRestaurantUseCase list;
    private final DeleteRestaurantUseCase delete;

    public RestaurantController(CreateRestaurantUseCase create, UpdateRestaurantUseCase update,
                                FindRestaurantUseCase find, ListRestaurantUseCase list, DeleteRestaurantUseCase delete) {
        this.create = create;
        this.update = update;
        this.find = find;
        this.list = list;
        this.delete = delete;
    }

    @Override
    @PostMapping({"", "/"})
    public ResponseEntity<RestaurantOutputDTO> create(@RequestBody RestaurantCreateInputDTO input,
                                                      @AuthenticationPrincipal RestaurantUserPrincipal user) {
        var output = create.execute(input, user.getId());
        var location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/restaurant/{id}").buildAndExpand(output.id()).toUri();
        return ResponseEntity.created(location).body(output);
    }

    @Override
    @PutMapping("/{id}")
    public RestaurantOutputDTO update(@PathVariable Long id, @RequestBody RestaurantUpdateInputDTO input,
                                      @AuthenticationPrincipal RestaurantUserPrincipal user) {
        return update.execute(input, id, user.getId());
    }

    @Override
    @GetMapping("/{id}")
    public RestaurantOutputDTO find(@PathVariable Long id) {
        return find.execute(id);
    }

    @Override
    @GetMapping({"", "/"})
    public Page<RestaurantOutputDTO> list(@RequestParam(required = false) Integer categoryId,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int offset) {
        return list.execute(categoryId, page, offset);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal RestaurantUserPrincipal user) {
        delete.execute(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
