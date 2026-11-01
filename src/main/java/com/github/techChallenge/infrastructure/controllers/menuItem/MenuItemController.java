package com.github.techChallenge.infrastructure.controllers.menuItem;

import com.github.techChallenge.application.usecases.menuItem.*;
import com.github.techChallenge.domain.menuItem.dto.*;
import com.github.techChallenge.infrastructure.security.RestaurantUserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/restaurant/{restaurantId}/menu")
public class MenuItemController implements IMenuItemController {
    private final CreateMenuItemUseCase create;
    private final UpdateMenuItemUseCase update;
    private final FindMenuItemUseCase find;
    private final ListMenuItemUseCase list;
    private final DeleteMenuItemUseCase delete;

    public MenuItemController(CreateMenuItemUseCase create, UpdateMenuItemUseCase update,
                              FindMenuItemUseCase find, ListMenuItemUseCase list, DeleteMenuItemUseCase delete) {
        this.create = create;
        this.update = update;
        this.find = find;
        this.list = list;
        this.delete = delete;
    }

    @Override
    @PostMapping({"", "/"})
    public ResponseEntity<MenuItemOutputDTO> create(@PathVariable Long restaurantId, @RequestBody MenuItemCreateInputDTO input,
                                                    @AuthenticationPrincipal RestaurantUserPrincipal user) {
        var output = create.execute(input, restaurantId, user.getId());
        var location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/restaurant/{restaurantId}/menu/{id}")
                .buildAndExpand(restaurantId, output.id()).toUri();
        return ResponseEntity.created(location).body(output);
    }

    @Override
    @PutMapping("/{id}")
    public MenuItemOutputDTO update(@PathVariable Long restaurantId, @PathVariable Long id, @RequestBody MenuItemUpdateInputDTO input,
                                    @AuthenticationPrincipal RestaurantUserPrincipal user) {
        return update.execute(input, restaurantId, id, user.getId());
    }

    @Override
    @GetMapping("/{id}")
    public MenuItemOutputDTO find(@PathVariable Long restaurantId, @PathVariable Long id) {
        return find.execute(restaurantId, id);
    }

    @Override
    @GetMapping({"", "/"})
    public Page<MenuItemOutputDTO> list(@PathVariable Long restaurantId,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int offset) {
        return list.execute(restaurantId, page, offset);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long restaurantId, @PathVariable Long id, @AuthenticationPrincipal RestaurantUserPrincipal user) {
        delete.execute(restaurantId, id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
