package com.github.techChallenge.infrastructure.controller;

import com.github.techChallenge.application.exceptions.GlobalExceptionHandler;
import com.github.techChallenge.application.exceptions.MenuItemNotFoundException;
import com.github.techChallenge.application.exceptions.ProblemDetailFactory;
import com.github.techChallenge.application.usecases.menuItem.*;
import com.github.techChallenge.application.usecases.restaurant.*;
import com.github.techChallenge.domain.menuItem.dto.*;
import com.github.techChallenge.domain.user.UserLevel;
import com.github.techChallenge.infrastructure.controllers.menuItem.MenuItemController;
import com.github.techChallenge.infrastructure.controllers.restaurant.RestaurantController;
import com.github.techChallenge.infrastructure.entities.user.UserEntity;
import com.github.techChallenge.infrastructure.repositories.user.UserRepository;
import com.github.techChallenge.infrastructure.security.RestaurantSecurityConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration;
import org.springframework.boot.data.mongodb.autoconfigure.DataMongoAutoConfiguration;
import org.springframework.boot.data.mongodb.autoconfigure.DataMongoRepositoriesAutoConfiguration;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.mongodb.autoconfigure.MongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RestaurantMenuOpenApiIntegrationTest.WebApplication.class)
class RestaurantMenuOpenApiIntegrationTest {
    private static final String MENU = "/api/v1/restaurant/7/menu";
    private static final String PASSWORD = "Senha@123";
    private static final String ITEM_JSON = """
            {"name":"Prato","description":"Prato da casa","price":29.90,
             "dineInOnly":false,"photoPath":"/fotos/prato.png"}
            """;
    private final HttpClient http = HttpClient.newHttpClient();
    @Autowired Environment environment;
    @Autowired ObjectMapper json;
    @MockitoBean UserRepository users;
    @MockitoBean CreateRestaurantUseCase createRestaurant;
    @MockitoBean UpdateRestaurantUseCase updateRestaurant;
    @MockitoBean FindRestaurantUseCase findRestaurant;
    @MockitoBean ListRestaurantUseCase listRestaurants;
    @MockitoBean DeleteRestaurantUseCase deleteRestaurant;
    @MockitoBean CreateMenuItemUseCase create;
    @MockitoBean UpdateMenuItemUseCase update;
    @MockitoBean FindMenuItemUseCase find;
    @MockitoBean ListMenuItemUseCase list;
    @MockitoBean DeleteMenuItemUseCase delete;

    @Configuration(proxyBeanMethods = false)
    @EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class,
            DataJpaRepositoriesAutoConfiguration.class, MongoAutoConfiguration.class,
            DataMongoAutoConfiguration.class, DataMongoRepositoriesAutoConfiguration.class})
    @Import({RestaurantController.class, MenuItemController.class, RestaurantSecurityConfiguration.class,
            GlobalExceptionHandler.class, ProblemDetailFactory.class})
    static class WebApplication {}

    @BeforeEach
    void prepareCredentialsWithoutDatabases() {
        for (var level : UserLevel.values()) {
            var user = mock(UserEntity.class);
            String login = level == UserLevel.OWNER ? "owner" : "customer";
            when(user.getId()).thenReturn(1L);
            when(user.getLogin()).thenReturn(login);
            when(user.getUserLevel()).thenReturn(level);
            when(user.getPassword()).thenReturn(new BCryptPasswordEncoder(4).encode(PASSWORD));
            when(users.findByLogin(login)).thenReturn(Optional.of(user));
        }
    }

    private MenuItemOutputDTO item() {
        return new MenuItemOutputDTO(10L, "Prato", "Prato da casa", new BigDecimal("29.90"),
                false, "/fotos/prato.png", 7L);
    }

    private HttpResponse<String> request(String method, String path, String payload, String login) throws Exception {
        var builder = HttpRequest.newBuilder(URI.create("http://127.0.0.1:"
                + environment.getProperty("local.server.port") + path));
        if (login != null) builder.header("Authorization", "Basic " + Base64.getEncoder()
                .encodeToString((login + ":" + PASSWORD).getBytes(StandardCharsets.UTF_8)));
        if (payload != null) builder.header("Content-Type", "application/json");
        builder.method(method, payload == null ? HttpRequest.BodyPublishers.noBody()
                : HttpRequest.BodyPublishers.ofString(payload));
        return http.send(builder.build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    private JsonNode specification() throws Exception {
        var response = request("GET", "/v3/api-docs", null, null);
        assertEquals(200, response.statusCode(), response.body());
        return json.readTree(response.body());
    }

    @Test
    void shouldDocumentBothCRUDsUnderRestaurantsUsingTheInterfaces() throws Exception {
        var spec = specification();
        for (var path : List.of("/api/v1/restaurant", "/api/v1/restaurant/{id}",
                "/api/v1/restaurant/{restaurantId}/menu", "/api/v1/restaurant/{restaurantId}/menu/{id}")) {
            var methods = path.endsWith("/{id}") ? List.of("get", "put", "delete") : List.of("get", "post");
            for (var method : methods) {
                var operation = spec.path("paths").path(path).path(method);
                assertFalse(operation.isMissingNode(), method + " " + path);
                assertEquals("Restaurantes", operation.path("tags").get(0).asText());
                assertFalse(operation.path("summary").asText().isBlank());
                String status = method.equals("post") ? "201" : method.equals("delete") ? "204" : "200";
                assertTrue(operation.path("responses").has(status));
                if (!method.equals("get")) {
                    assertTrue(operation.path("security").get(0).has("basicAuth"));
                    assertTrue(operation.path("responses").has("401"));
                    assertTrue(operation.path("responses").has("403"));
                }
            }
        }
        assertEquals("basic", spec.path("components").path("securitySchemes").path("basicAuth").path("scheme").asText());
        assertFalse(spec.path("paths").has("/api/v1/menu-item"));
        assertFalse(spec.path("paths").has("/api/v1/menu-item/{id}"));
    }

    @Test
    void shouldDocumentRestaurantAsARequiredPathParameterAndHideThePrincipal() throws Exception {
        var spec = specification();
        var path = spec.path("paths").path("/api/v1/restaurant/{restaurantId}/menu");
        var parameters = path.path("post").path("parameters");
        assertEquals(1, parameters.size());
        assertEquals("restaurantId", parameters.get(0).path("name").asText());
        assertEquals("path", parameters.get(0).path("in").asText());
        assertTrue(parameters.get(0).path("required").asBoolean());
        assertFalse(spec.path("components").path("schemas").path("MenuItemCreateInputDTO").path("properties").has("restaurantId"));
        assertTrue(spec.path("components").path("schemas").path("MenuItemCreateInputDTO").path("properties").has("price"));
        for (var parameter : path.path("get").path("parameters")) {
            if (parameter.path("name").asText().equals("restaurantId")) assertEquals("path", parameter.path("in").asText());
        }
    }

    @Test
    void shouldServeTheSwaggerPage() throws Exception {
        assertEquals(200, request("GET", "/swagger-ui/index.html", null, null).statusCode());
    }

    @Test
    void shouldCreateUsingTheRestaurantFromThePathAndReturnTheNestedLocation() throws Exception {
        when(create.execute(any(MenuItemCreateInputDTO.class), eq(7L), eq(1L))).thenReturn(item());
        var response = request("POST", MENU, ITEM_JSON, "owner");
        assertEquals(201, response.statusCode(), response.body());
        assertTrue(response.headers().firstValue("Location").orElse("").endsWith(MENU + "/10"));
        verify(create).execute(new MenuItemCreateInputDTO("Prato", "Prato da casa", new BigDecimal("29.90"),
                false, "/fotos/prato.png"), 7L, 1L);
    }

    @Test
    void shouldListOnlyTheRestaurantFromThePathWithDefaultPagination() throws Exception {
        when(list.execute(7L, 0, 10)).thenReturn(new PageImpl<>(List.of(item())));
        var response = request("GET", MENU, null, null);
        assertEquals(200, response.statusCode(), response.body());
        assertEquals(7L, json.readTree(response.body()).path("content").get(0).path("restaurantId").asLong());
        verify(list).execute(7L, 0, 10);
    }

    @Test
    void shouldBindCustomPagination() throws Exception {
        when(list.execute(7L, 2, 5)).thenReturn(new PageImpl<>(List.of()));
        assertEquals(200, request("GET", MENU + "?page=2&offset=5", null, null).statusCode());
        verify(list).execute(7L, 2, 5);
    }

    @Test
    void shouldFindUpdateAndDeleteUsingBothPathIds() throws Exception {
        when(find.execute(7L, 10L)).thenReturn(item());
        when(update.execute(any(MenuItemUpdateInputDTO.class), eq(7L), eq(10L), eq(1L))).thenReturn(item());
        assertEquals(200, request("GET", MENU + "/10", null, null).statusCode());
        assertEquals(200, request("PUT", MENU + "/10", ITEM_JSON, "owner").statusCode());
        assertEquals(204, request("DELETE", MENU + "/10", null, "owner").statusCode());
        verify(find).execute(7L, 10L);
        verify(update).execute(any(MenuItemUpdateInputDTO.class), eq(7L), eq(10L), eq(1L));
        verify(delete).execute(7L, 10L, 1L);
    }

    @Test
    void shouldKeepPublicReadsAndRequireOwnerCredentialsForWrites() throws Exception {
        assertEquals(401, request("POST", MENU, ITEM_JSON, null).statusCode());
        assertEquals(403, request("POST", MENU, ITEM_JSON, "customer").statusCode());
        verifyNoInteractions(create);
    }

    @Test
    void shouldReturnProblemDetailWhenTheItemDoesNotBelongToTheRestaurant() throws Exception {
        when(find.execute(7L, 22L)).thenThrow(new MenuItemNotFoundException(22L));
        var response = request("GET", MENU + "/22", null, null);
        assertEquals(404, response.statusCode(), response.body());
        assertTrue(response.headers().firstValue("Content-Type").orElse("").startsWith("application/problem+json"));
        assertEquals(404, json.readTree(response.body()).path("status").asInt());
    }
}
