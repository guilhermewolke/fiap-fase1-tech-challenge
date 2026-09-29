package com.github.techChallenge.infrastructure.controllers;

import com.github.techChallenge.application.usecases.user.*;
import com.github.techChallenge.application.validators.UserValidator;
import com.github.techChallenge.domain.user.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController implements IUserController {

    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final FindUserUseCase   findUserUseCase;
    private final ListUserUseCase   listUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final UserValidator     userValidator;
    private final ChangePasswordUseCase changePasswordUser;

    public UserController(CreateUserUseCase createUserUseCase,
                          UpdateUserUseCase updateUserUseCase,
                          FindUserUseCase findUserUseCase,
                          ListUserUseCase listUserUseCase,
                          DeleteUserUseCase deleteUserUseCase, UserValidator userValidator, ChangePasswordUseCase changePasswordUser
    ) {
        this.createUserUseCase = createUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.findUserUseCase = findUserUseCase;
        this.listUserUseCase = listUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.userValidator = userValidator;
        this.changePasswordUser = changePasswordUser;
    }

    @PostMapping("/")
    @Override
    public ResponseEntity<UserOutputDTO> create(
            @Valid @RequestBody UserCreateInputDTO dto) {
        UserOutputDTO response = this.createUserUseCase.execute(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<UserOutputDTO> update(
            @Valid @RequestBody UserUpdateInputDTO dto,
            @PathVariable("id") Long id
    ) {
        UserOutputDTO response = this.updateUserUseCase.execute(dto, id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<UserOutputDTO> findByID(@PathVariable("id") Long id) {
        UserOutputDTO dto = this.findUserUseCase.execute(id);

        return ResponseEntity.ok(dto);
    }

    @Override
    @GetMapping("/find-by-name")
    public ResponseEntity<Page<UserOutputDTO>> list(
        @RequestParam("name") String name,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "offset", defaultValue = "10") int offset) {
        Page<UserOutputDTO> users = this.listUserUseCase.execute(name, page, offset);

        return ResponseEntity.ok(users);
    }

    @Override
    @GetMapping("/")
    public ResponseEntity<Page<UserOutputDTO>> list(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "offset", defaultValue = "10") int offset) {
        Page<UserOutputDTO> users = this.listUserUseCase.execute(page, offset);

        return ResponseEntity.ok(users);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        this.deleteUserUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/change-password/{id}")
    public ResponseEntity<Map<String, Object>> changePassword(
            @PathVariable("id") Long id,
            @Valid @RequestBody UserChangePasswordInputDTO dto) {
        boolean sucess = this.changePasswordUser.changePassword(dto);
        if(!sucess){ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT);};
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Senha alterada");

        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping("/auth-login")
    public ResponseEntity<Map<String, Object>> authUser(
            @Valid @RequestBody UserAuthInputDTO dto) {
       boolean sucess = this.userValidator.authUser(dto);
        if(!sucess){ResponseEntity.status(HttpStatus.UNAUTHORIZED);};
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Usuário autenticado");

        return ResponseEntity.ok(response);
    }

}
