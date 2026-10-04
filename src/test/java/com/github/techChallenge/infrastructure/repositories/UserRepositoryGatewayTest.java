package com.github.techChallenge.infrastructure.repositories;

import com.github.techChallenge.application.exceptions.UserNotFoundException;
import com.github.techChallenge.domain.user.Address;
import com.github.techChallenge.domain.user.User;
import com.github.techChallenge.infrastructure.repositories.user.UserRepositoryGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = {"/00_cleanup-tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/00_cleanup-tables.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Transactional
class UserRepositoryGatewayTest {

    @Autowired
    private UserRepositoryGateway userRepositoryGateway;

    @Test
    @DisplayName("Deve criar um usuário no banco de dados")

    void shouldCreateANewUserOnDatabase() {
        User user = User.create(
            "User 1",
                "user@user.com.br",
                "user",
                "password",
                new Address(
                    "Endereço",
                    "numero",
                    "complemento",
                    "01234-567",
                    "bairro",
                    "cidade",
                    "uf",
                    "Brasil"
                )
        );
        User saved = userRepositoryGateway.create(user);

        assertNotNull(saved.getId());
        System.out.println(saved.getId());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
        assertEquals(user.getName(), saved.getName());
        assertEquals(user.getEmail(), saved.getEmail());
        assertEquals(user.getLogin(), saved.getLogin());
        assertEquals(user.getPassword(), saved.getPassword());
        assertEquals(user.getAddress().zipCode(), saved.getAddress().zipCode());
        assertEquals(user.getAddress().state(), saved.getAddress().state());
        assertEquals(user.getAddress().number(), saved.getAddress().number());
        assertEquals(user.getAddress().neighborhood(), saved.getAddress().neighborhood());
        assertEquals(user.getAddress().country(), saved.getAddress().country());
        assertEquals(user.getAddress().complement(), saved.getAddress().complement());
        assertEquals(user.getAddress().city(), saved.getAddress().city());
        assertEquals(user.getAddress().address(), saved.getAddress().address());
    }

    @Test
    @DisplayName("Deve atualizar um usuário já existente no banco de dados")
    @Sql(scripts = "/01_users_level_samples.sql")
    @Sql(scripts = "/02_users_samples.sql")
    void shouldUpdateAnExistingUserOnDatabase() {
        User user = userRepositoryGateway.findByID(1L);
        user.update("Usuário alterado", "email alterado", "login alterado",
                new Address(
                    "endereço alterado",
                    "numero alterado",
                    "complemento alterado",
                    "89101-121",
                    "Bairro alterado",
                    "cidade alterado",
                    "uf alterado",
                    "Argentina"
                ));
        User saved = userRepositoryGateway.update(user, 1L);

        assertEquals(1L, saved.getId());
        assertEquals(LocalDateTime.parse("2026-09-30T17:47:40"), saved.getCreatedAt());
        assertNotEquals(saved.getCreatedAt(), saved.getUpdatedAt());
        assertEquals("Usuário alterado", saved.getName());
        assertEquals("email alterado", saved.getEmail());
        assertEquals("login alterado", saved.getLogin());
        assertEquals(user.getPassword(), saved.getPassword());
        assertEquals("89101-121", saved.getAddress().zipCode());
        assertEquals("uf alterado", saved.getAddress().state());
        assertEquals("numero alterado", saved.getAddress().number());
        assertEquals("Bairro alterado", saved.getAddress().neighborhood());
        assertEquals("Argentina", saved.getAddress().country());
        assertEquals("complemento alterado", saved.getAddress().complement());
        assertEquals("cidade alterado", saved.getAddress().city());
        assertEquals("endereço alterado", saved.getAddress().address());
    }

    @Test
    @DisplayName("Deve lançar uma exception caso tente editar um usuário que não existe")
    @Sql(scripts = "/01_users_level_samples.sql")
    @Sql(scripts = "/02_users_samples.sql")
    void shouldThrowAnExceptionOnTryingToUpdateAnUserThatDoesNotExists() {
        User user = userRepositoryGateway.findByID(1L);
        user.update("Usuário alterado", "email alterado", "login alterado", new Address(
                    "endereço alterado",
                    "numero alterado",
                    "complemento alterado",
                    "89101-121",
                    "Bairro alterado",
                    "cidade alterado",
                    "uf alterado",
                    "Argentina"
                ));
        assertThrows(UserNotFoundException.class, () ->  userRepositoryGateway.update(user, 4L));
    }

    @Test
    @DisplayName("Deve retornar uma lista de usuários cujo nome seja parecido com o texto enviado para busca")
    @Sql(scripts = "/01_users_level_samples.sql")
    @Sql(scripts = "/02_users_samples.sql")
    void shouldReturnAListWithUsersWhichNameMatchesTheKeywordSent() {
        Page<User> usersList = userRepositoryGateway.listByName("Usuário", 0, 10);
        User firstUser = usersList.getContent().getFirst();
        User lastUser = usersList.getContent().getLast();
        assertEquals(1L, firstUser.getId());
        assertEquals("Usuário 1", firstUser.getName());
        assertEquals(LocalDateTime.parse("2026-09-30T17:47:40"), firstUser.getCreatedAt());
        assertEquals("email@email.com.br", firstUser.getEmail());
        assertEquals("email@email.com.br", firstUser.getEmail());
        assertEquals("user1", firstUser.getLogin());
        assertEquals("Rua 1", firstUser.getAddress().address());
        assertEquals("Cidade", firstUser.getAddress().city());
        assertEquals("N/A", firstUser.getAddress().complement());
        assertEquals("Brasil", firstUser.getAddress().country());
        assertEquals("Centro", firstUser.getAddress().neighborhood());
        assertEquals("123", firstUser.getAddress().number());
        assertEquals("SP", firstUser.getAddress().state());
        assertEquals("01234-567", firstUser.getAddress().zipCode());

        assertEquals(3L, lastUser.getId());
        assertEquals("Usuário 3", lastUser.getName());
        assertEquals(LocalDateTime.parse("2026-10-01T11:22:40"), lastUser.getCreatedAt());
        assertEquals("email3@email.com.br", lastUser.getEmail());
        assertEquals("user3", lastUser.getLogin());
        assertEquals("Rua 3", lastUser.getAddress().address());
        assertEquals("Cidade 3", lastUser.getAddress().city());
        assertEquals("N/A 3", lastUser.getAddress().complement());
        assertEquals("Uruguai", lastUser.getAddress().country());
        assertEquals("Jardines", lastUser.getAddress().neighborhood());
        assertEquals("789", lastUser.getAddress().number());
        assertEquals("SC", lastUser.getAddress().state());
        assertEquals("31415-161", lastUser.getAddress().zipCode());
    }

    @Test
    @DisplayName("Deve localizar um usuário pelo seu ID")
    @Sql(scripts = "/01_users_level_samples.sql")
    @Sql(scripts = "/02_users_samples.sql")
    void shouldReturnAnUserFindingHimByHisID() {
        User user = userRepositoryGateway.findByID(2L);

        assertEquals(2L, user.getId());
        assertEquals("Usuária 2", user.getName());
        assertEquals(LocalDateTime.parse("2026-09-30T18:30:40"), user.getCreatedAt());
        assertEquals(LocalDateTime.parse("2026-09-30T18:30:40"), user.getUpdatedAt());
        assertEquals("email2@email.com.br", user.getEmail());
        assertEquals("user2", user.getLogin());
        assertEquals("Rua 2", user.getAddress().address());
        assertEquals("Cidade 2", user.getAddress().city());
        assertEquals("N/A 2", user.getAddress().complement());
        assertEquals("Argentina", user.getAddress().country());
        assertEquals("Jardines", user.getAddress().neighborhood());
        assertEquals("456", user.getAddress().number());
        assertEquals("RJ", user.getAddress().state());
        assertEquals("89011-121", user.getAddress().zipCode());
    }

    @Test
    @DisplayName("Deve lançar uma exception caso não consiga localizar um usuário pelo ID")
    @Sql(scripts = "/01_users_level_samples.sql")
    @Sql(scripts = "/02_users_samples.sql")
    void shouldThrowAnExceptionIfUserWasNotFound() {
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userRepositoryGateway.findByID(7L));
        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar uma lista de usuários")
    @Sql(scripts = "/01_users_level_samples.sql")
    @Sql(scripts = "/02_users_samples.sql")
    void shouldReturnAnUserList() {
        Page<User> usersList = userRepositoryGateway.list(0, 10);
        User firstUser = usersList.getContent().getFirst();
        User secondUser = usersList.getContent().get(1);
        User lastUser = usersList.getContent().getLast();

        assertEquals(1L, firstUser.getId());
        assertEquals("Usuário 1", firstUser.getName());
        assertEquals(LocalDateTime.parse("2026-09-30T17:47:40"), firstUser.getCreatedAt());
        assertEquals("email@email.com.br", firstUser.getEmail());
        assertEquals("user1", firstUser.getLogin());
        assertEquals("Rua 1", firstUser.getAddress().address());
        assertEquals("Cidade", firstUser.getAddress().city());
        assertEquals("N/A", firstUser.getAddress().complement());
        assertEquals("Brasil", firstUser.getAddress().country());
        assertEquals("Centro", firstUser.getAddress().neighborhood());
        assertEquals("123", firstUser.getAddress().number());
        assertEquals("SP", firstUser.getAddress().state());
        assertEquals("01234-567", firstUser.getAddress().zipCode());

        assertEquals(2L, secondUser.getId());
        assertEquals("Usuária 2", secondUser.getName());
        assertEquals(LocalDateTime.parse("2026-09-30T18:30:40"), secondUser.getCreatedAt());
        assertEquals(LocalDateTime.parse("2026-09-30T18:30:40"), secondUser.getUpdatedAt());
        assertEquals("email2@email.com.br", secondUser.getEmail());
        assertEquals("user2", secondUser.getLogin());
        assertEquals("Rua 2", secondUser.getAddress().address());
        assertEquals("Cidade 2", secondUser.getAddress().city());
        assertEquals("N/A 2", secondUser.getAddress().complement());
        assertEquals("Argentina", secondUser.getAddress().country());
        assertEquals("Jardines", secondUser.getAddress().neighborhood());
        assertEquals("456", secondUser.getAddress().number());
        assertEquals("RJ", secondUser.getAddress().state());
        assertEquals("89011-121", secondUser.getAddress().zipCode());

        assertEquals(3L, lastUser.getId());
        assertEquals("Usuário 3", lastUser.getName());
        assertEquals(LocalDateTime.parse("2026-10-01T11:22:40"), lastUser.getCreatedAt());
        assertEquals("email3@email.com.br", lastUser.getEmail());
        assertEquals("user3", lastUser.getLogin());
        assertEquals("Rua 3", lastUser.getAddress().address());
        assertEquals("Cidade 3", lastUser.getAddress().city());
        assertEquals("N/A 3", lastUser.getAddress().complement());
        assertEquals("Uruguai", lastUser.getAddress().country());
        assertEquals("Jardines", lastUser.getAddress().neighborhood());
        assertEquals("789", lastUser.getAddress().number());
        assertEquals("SC", lastUser.getAddress().state());
        assertEquals("31415-161", lastUser.getAddress().zipCode());
    }

    @Test
    @DisplayName("Deve remover um usuário pelo ID")
    @Sql(scripts = "/01_users_level_samples.sql")
    @Sql(scripts = "/02_users_samples.sql")
    void shouldRemoveAndUserByItsID() {
        userRepositoryGateway.delete(1L);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userRepositoryGateway.findByID(1L));
        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar uma exception caso não consiga remover um usuário, pois nenhum usuário com o ID informado foi localizado")
    @Sql(scripts = "/01_users_level_samples.sql")
    @Sql(scripts = "/02_users_samples.sql")
    void shouldThrowAnExceptionOnTryingToDeleteAnInexistentUser() {
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userRepositoryGateway.delete(7L));
        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar true se já existir algum usuário com o email informado")
    @Sql(scripts = "/01_users_level_samples.sql")
    @Sql(scripts = "/02_users_samples.sql")
    void shouldReturnTrueIfThereIsAlreadyAnUserWithThisEmail() {
        assertTrue(userRepositoryGateway.existsByEmail("email@email.com.br"));
    }

    @Test
    @DisplayName("Deve retornar false se não existir algum usuário com o email informado")
    @Sql(scripts = "/01_users_level_samples.sql")
    @Sql(scripts = "/02_users_samples.sql")
    void shouldReturnFalseIfThereAreNotAnyUsersWithThisEmail() {
        assertFalse(userRepositoryGateway.existsByEmail("email_@email.com.br"));
    }

    @Test
    @DisplayName("Deve retornar true se já existir algum usuário com o login informado")
    @Sql(scripts = "/01_users_level_samples.sql")
    @Sql(scripts = "/02_users_samples.sql")
    void shouldReturnTrueIfThereIsAlreadyAnUserWithThisLogin() {
        assertTrue(userRepositoryGateway.existsByLogin("user1"));
    }

    @Test
    @DisplayName("Deve retornar false se não existir algum usuário com o login informado")
    @Sql(scripts = "/01_users_level_samples.sql")
    @Sql(scripts = "/02_users_samples.sql")
    void shouldReturnFalseIfThereAreNotAnyUsersWithThisLogin() {
        assertFalse(userRepositoryGateway.existsByLogin("user10"));
    }

    @Test
    void getEncryptPasswordByLogin() {
        fail("Testar se a senha é retornada, e também realizar um teste cobrindo o comportamento caso o login não exista");
    }

    @Test
    @DisplayName("Deve atualizar a senha do usuário pelo login")
    @Sql(scripts = "/01_users_level_samples.sql")
    @Sql(scripts = "/02_users_samples.sql")
    void shouldUpdateAnUsersPasswordByHisLogin() {
        String newEncryptedPassword = "pitipiripitipiripitipó";
        boolean success = userRepositoryGateway.updatePasswordByLogin(newEncryptedPassword, "user1");
        assertTrue(success);
        User user = userRepositoryGateway.findByID(1L);
        assertEquals(newEncryptedPassword, user.getPassword());
    }

    @Test
    @DisplayName("Deve lançar exception se não localizar nenhum usuário com o login")
    @Sql(scripts = "/01_users_level_samples.sql")
    @Sql(scripts = "/02_users_samples.sql")
    void shouldThrowAnExceptionWhenUserWasNotFoundDuringPasswordUpdating() {
        String newEncryptedPassword = "pitipiripitipiripitipó";
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userRepositoryGateway.updatePasswordByLogin(newEncryptedPassword, "user11"));
        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar true se existir algum usuário com o email informado, e com id diferente")
    @Sql(scripts = "/01_users_level_samples.sql")
    @Sql(scripts = "/02_users_samples.sql")
    void shouldReturnTrueIfThereIsAnUserWithThisEmailAndDifferentID() {
        assertTrue(userRepositoryGateway.existsByEmailAndIdNot("email2@email.com.br", 1L));
    }

    @Test
    @DisplayName("Deve retornar false se não existir algum usuário com o email informado, e com id diferente")
    @Sql(scripts = "/01_users_level_samples.sql")
    @Sql(scripts = "/02_users_samples.sql")
    void shouldReturnFalseIfThereAreNotAnyUsersWithThisEmailAndDifferentID() {
        assertFalse(userRepositoryGateway.existsByEmailAndIdNot("email@email.com.br", 1L));
    }

    @Test
    @DisplayName("Deve retornar true se existir algum usuário com o login informado, e com id diferente")
    @Sql(scripts = "/01_users_level_samples.sql")
    @Sql(scripts = "/02_users_samples.sql")
    void shouldReturnTrueIfThereIsAnUserWithThisLoginAndDifferentID() {
        assertTrue(userRepositoryGateway.existsByLoginAndIdNot("user2", 1L));
    }

    @Test
    @DisplayName("Deve retornar false se não existir algum usuário com o login informado, e com id diferente")
    @Sql(scripts = "/01_users_level_samples.sql")
    @Sql(scripts = "/02_users_samples.sql")
    void shouldReturnFalseIfThereAreNotAnyUsersWithThisLoginAndDifferentID() {
        assertFalse(userRepositoryGateway.existsByLoginAndIdNot("user1", 1L));
    }

    @Test
    @DisplayName("Deve retornar true se existir algum usuário com o ID informado")
    @Sql(scripts = "/01_users_level_samples.sql")
    @Sql(scripts = "/02_users_samples.sql")
    void shouldReturnTrueIfThereIsAnUserWithThisID() {
        assertTrue(userRepositoryGateway.existsById(1L));
    }

    @Test
    @DisplayName("Deve retornar false se não existir algum usuário com o ID informado")
    @Sql(scripts = "/01_users_level_samples.sql")
    @Sql(scripts = "/02_users_samples.sql")
    void shouldReturnFalseIfThereAreNotAnyUsersWithThisID() {
        assertFalse(userRepositoryGateway.existsById(11L));
    }

}