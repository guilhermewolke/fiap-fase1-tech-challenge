package com.github.techChallenge.domain.user;

import com.github.techChallenge.domain.userlevel.UserLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        Address address = new Address("Rua 1","123","N/A",
                "11235-800","Bairro","Cidade",
                "UF","Brasil");

        this.user = User.create("User 1","user@user.com.br","user",
                "password", address);
    }

    @Test
    @DisplayName("Deve criar um novo usuário")
    void shouldCreateANewUser() {
        assertNull(user.getId());
        assertEquals("User 1", user.getName());
        assertEquals("user@user.com.br", user.getEmail());
        assertEquals("user", user.getLogin());
        assertEquals("password", user.getPassword());
        assertEquals("Rua 1", user.getAddress().address());
        assertEquals("123", user.getAddress().number());
        assertEquals("N/A", user.getAddress().complement());
        assertEquals("11235-800", user.getAddress().zipCode());
        assertEquals("Bairro", user.getAddress().neighborhood());
        assertEquals("Cidade", user.getAddress().city());
        assertEquals("UF", user.getAddress().state());
        assertEquals("Brasil", user.getAddress().country());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        assertEquals(user.getCreatedAt(), user.getUpdatedAt());
    }

    @Test
    @DisplayName("Deve Lançar uma exceção devido a dados inválidos durante a criação")
    void shouldThrowAnExceptionDueToInvalidDataDuringCreation() {
        fail("implementar método de validação na entidade, e chamar ele na hora de criar um novo usuário");
    }

    @Test
    @DisplayName("Deve atualizar os dados de um usuário existente")
    void shouldUpdateAnExistingUser() {
        //Updating this user
        Address address = new Address("Rua 2","1234","N/A alterado",
                "11235-900","Bairro alterado","Cidade alterado",
                "UF alterado","Argentinha");

        user.update("User 2", "user_alterado@user.com.br", "user_alterado", address);

        assertNull(user.getId());
        assertEquals("User 2", user.getName());
        assertEquals("user_alterado@user.com.br", user.getEmail());
        assertEquals("user_alterado", user.getLogin());
        assertEquals("password", user.getPassword());
        assertEquals("Rua 2", user.getAddress().address());
        assertEquals("1234", user.getAddress().number());
        assertEquals("N/A alterado", user.getAddress().complement());
        assertEquals("11235-900", user.getAddress().zipCode());
        assertEquals("Bairro alterado", user.getAddress().neighborhood());
        assertEquals("Cidade alterado", user.getAddress().city());
        assertEquals("UF alterado", user.getAddress().state());
        assertEquals("Argentinha", user.getAddress().country());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        assertNotEquals(user.getCreatedAt(), user.getUpdatedAt());
    }

    @Test
    @DisplayName("Deve lançar uma excepção devido a erros de validação durante update")
    void shouldThrowAnExceptionDueToInvalidDataDuringUpdate() {
        fail("implementar método de validação na entidade, e chamar ele na hora de atualizar os dados do usuário");
    }

    @Test
    @DisplayName("Deve alterar a senha de um usuário")
    void shouldChangePasswordOfAUser() {
        user.changePassword("nova senha");

        assertNull(user.getId());
        assertEquals("User 1", user.getName());
        assertEquals("user@user.com.br", user.getEmail());
        assertEquals("user", user.getLogin());
        assertEquals("nova senha", user.getPassword());
        assertEquals("Rua 1", user.getAddress().address());
        assertEquals("123", user.getAddress().number());
        assertEquals("N/A", user.getAddress().complement());
        assertEquals("11235-800", user.getAddress().zipCode());
        assertEquals("Bairro", user.getAddress().neighborhood());
        assertEquals("Cidade", user.getAddress().city());
        assertEquals("UF", user.getAddress().state());
        assertEquals("Brasil", user.getAddress().country());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        assertNotEquals(user.getCreatedAt(), user.getUpdatedAt());
    }


}