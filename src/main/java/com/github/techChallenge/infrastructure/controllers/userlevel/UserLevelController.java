package com.github.techChallenge.infrastructure.controllers.userlevel;

import com.github.techChallenge.application.usecases.userlevel.*;
import com.github.techChallenge.application.validators.UserLevelValidator;
import com.github.techChallenge.domain.user.dto.*;
import com.github.techChallenge.domain.userlevel.dto.UserLevelCreateInputDTO;
import com.github.techChallenge.domain.userlevel.dto.UserLevelOutputDTO;
import com.github.techChallenge.domain.userlevel.dto.UserLevelUpdateInputDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/user-level", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserLevelController implements IUserLevelController {

    private final CreateUserLevelUseCase createUserLevelUseCase;
    private final UpdateUserLevelUseCase updateUserLevelUseCase;
    private final FindUserLevelUseCase findUserLevelUseCase;
    private final ListUserLevelUseCase listUserLevelUseCase;
    private final DeleteUserLevelUseCase deleteUserLevelUseCase;
    private final UserLevelValidator userLevelValidator;

    public UserLevelController(CreateUserLevelUseCase createUserLevelUseCase,
                               UpdateUserLevelUseCase updateUserLevelUseCase,
                               FindUserLevelUseCase findUserLevelUseCase,
                               ListUserLevelUseCase listUserLevelUseCase,
                               DeleteUserLevelUseCase deleteUserLevelUseCase, UserLevelValidator userLevelValidator
    ) {
        this.createUserLevelUseCase = createUserLevelUseCase;
        this.updateUserLevelUseCase = updateUserLevelUseCase;
        this.findUserLevelUseCase = findUserLevelUseCase;
        this.listUserLevelUseCase = listUserLevelUseCase;
        this.deleteUserLevelUseCase = deleteUserLevelUseCase;
        this.userLevelValidator = userLevelValidator;
    }

    @PostMapping("/")
    @Override
    public ResponseEntity<UserLevelOutputDTO> create(
            @Valid @RequestBody UserLevelCreateInputDTO dto) {
        UserLevelOutputDTO response = this.createUserLevelUseCase.execute(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<UserLevelOutputDTO> update(
            @Valid @RequestBody UserLevelUpdateInputDTO dto,
            @PathVariable("id") int id
    ) {
        UserLevelOutputDTO response = this.updateUserLevelUseCase.execute(dto, id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<UserLevelOutputDTO> findByID(@PathVariable("id") int id) {
        UserLevelOutputDTO dto = this.findUserLevelUseCase.execute(id);

        return ResponseEntity.ok(dto);
    }

    @Override
    @GetMapping("/")
    public ResponseEntity<Page<UserLevelOutputDTO>> list(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "offset", defaultValue = "10") int offset) {
        Page<UserLevelOutputDTO> users = this.listUserLevelUseCase.execute(page, offset);

        return ResponseEntity.ok(users);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        this.deleteUserLevelUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

}
