package com.github.techChallenge.infrastructure.config;


import com.github.techChallenge.application.gateways.restaurantCategory.IRestaurantCategoryGateway;
import com.github.techChallenge.application.gateways.restaurantCategory.RestaurantCategoryGateway;
import com.github.techChallenge.application.repositories.IRestaurantCategoryRepository;
import com.github.techChallenge.application.usecases.restaurantCategory.*;
import com.github.techChallenge.application.validators.RestaurantCategoryValidator;
import com.github.techChallenge.domain.restaurantCategory.IRestaurantCategoryMapper;
import com.github.techChallenge.infrastructure.mappers.RestaurantCategoryMapper;
import com.github.techChallenge.infrastructure.repositories.restaurantCategory.RestaurantCategoryRepositoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.util.List;

//Teste do RestaurantCategoryConfig
@ExtendWith(MockitoExtension.class)
public class RestaurantCategoryConfigTest {

    //Campos tem que estarem publicos para serem lidos
    @Mock
    private IRestaurantCategoryGateway gateway;

    @Mock
    private RestaurantCategoryMapper mapper;

    @Mock
    private RestaurantCategoryValidator validator;

    @Mock
    private IRestaurantCategoryRepository repository;

    private RestaurantCategoryConfig config;

    @BeforeEach
    void setUp() {
        config = new RestaurantCategoryConfig();
    }


    @Test
    @DisplayName("Deve criar CreateUserTypeUseCase com gateway, mapper e validator")
    void deveGerarCreateRestaurantCategoryUseCase() throws IllegalAccessException {
        //Arrange
        RestaurantCategoryConfig config = new RestaurantCategoryConfig();

        // Action
        var createRestaurantCategoryUseCase = config.createRestaurantCategoryUseCase(gateway, mapper);

        //Assert
        Assertions.assertNotNull(createRestaurantCategoryUseCase);
        Assertions.assertSame(gateway, ReflectionTestUtils.getField(createRestaurantCategoryUseCase, "gateway"));
        Assertions.assertSame(mapper, ReflectionTestUtils.getField(createRestaurantCategoryUseCase, "mapper"));

    }

    @Test
    @DisplayName("Deve criar UpdateRestaurantCategoryUseCase com gateway e mapper")
    void deveGerarUpdateRestaurantCategoryUseCase() throws IllegalAccessException {
        // Act
        UpdateRestaurantCategoryUseCase updateRestaurantCategoryUseCase = config.updateRestaurantCategoryUseCase(gateway, mapper, validator);

        // Assert
        Assertions.assertNotNull(updateRestaurantCategoryUseCase);
        Assertions.assertSame(gateway, ReflectionTestUtils.getField(updateRestaurantCategoryUseCase, "gateway"));
        Assertions.assertSame(mapper, ReflectionTestUtils.getField(updateRestaurantCategoryUseCase, "mapper"));
        Assertions.assertSame(validator, ReflectionTestUtils.getField(updateRestaurantCategoryUseCase, "restaurantCategoryValidator"));
    }

    @Test
    @DisplayName("Deve criar FindRestaurantCategoryUseCase com gateway e mapper")
    void deveGerarFindRestaurantCategoryUseCase() throws IllegalAccessException {
        // Act
        FindRestaurantCategoryUseCase findRestaurantCategoryUseCase = config.findRestaurantCategoryUseCase(gateway, mapper);

        // Assert
        Assertions.assertNotNull(findRestaurantCategoryUseCase);
        Assertions.assertSame(gateway, ReflectionTestUtils.getField(findRestaurantCategoryUseCase, "gateway"));
        Assertions.assertSame(mapper, ReflectionTestUtils.getField(findRestaurantCategoryUseCase, "mapper"));
    }


    @Test
    @DisplayName("Deve criar ListRestaurantCategoryUseCase com gateway e mapper")
    void deveGerarListRestaurantCategoryUseCase() throws IllegalAccessException {
        // Act
        ListRestaurantCategoryUseCase listRestaurantCategoryUseCase = config.listRestaurantCategoryUseCase(gateway, mapper);

        // Assert
        Assertions.assertNotNull(listRestaurantCategoryUseCase);
        Assertions.assertSame(gateway, ReflectionTestUtils.getField(listRestaurantCategoryUseCase, "gateway"));
        Assertions.assertSame(mapper, ReflectionTestUtils.getField(listRestaurantCategoryUseCase, "mapper"));
    }

    @Test
    @DisplayName("Deve criar DeleteRestaurantCategoryUseCase com gateway e mapper")
    void deveGerarDeleteRestaurantCategoryUseCase() throws IllegalAccessException {
        // Act
        DeleteRestaurantCategoryUseCase deleteRestaurantCategoryUseCase = config.deleteRestaurantCategoryUseCase(gateway, mapper);

        // Assert
        Assertions.assertNotNull(deleteRestaurantCategoryUseCase);
        Assertions.assertSame(gateway, ReflectionTestUtils.getField(deleteRestaurantCategoryUseCase, "gateway"));
        Assertions.assertSame(mapper, ReflectionTestUtils.getField(deleteRestaurantCategoryUseCase, "mapper"));
    }

    @Test
    @DisplayName("Deve criar RestaurantCategoryValidator com gateway e mapper")
    void deveGerarRestaurantCategoryValidator() throws IllegalAccessException {
        // Act
        RestaurantCategoryValidator restaurantCategoryValidator = config.restaurantCategoryValidator(gateway, mapper);

        // Assert
        Assertions.assertNotNull(restaurantCategoryValidator);
        Assertions.assertSame(gateway, ReflectionTestUtils.getField(restaurantCategoryValidator, "gateway"));
        Assertions.assertSame(mapper, ReflectionTestUtils.getField(restaurantCategoryValidator, "mapper"));
    }

    @Test
    @DisplayName("Deve criar RestaurantCategoryGateway com repositoryGateway e mapper")
    void deveGerarRestaurantCategoryGateway() throws IllegalAccessException {
        // Act
        RestaurantCategoryGateway restaurantCategoryGateway = config.restaurantCategoryGateway(repository, mapper);

        // Assert
        Assertions.assertNotNull(restaurantCategoryGateway);
        Assertions.assertInstanceOf(IRestaurantCategoryGateway.class, restaurantCategoryGateway);
        Assertions.assertSame(repository, ReflectionTestUtils.getField(restaurantCategoryGateway, "repository"));
        Assertions.assertSame(mapper, ReflectionTestUtils.getField(restaurantCategoryGateway, "mapper"));
    }

    @Test
    @DisplayName("Deve criar RestaurantCategoryRepositoryGateway")
    void deveGerarRestaurantCategoryRepositoryGateway() {
        // Act
        RestaurantCategoryRepositoryGateway created = config.restaurantCategoryRepositoryGateway();

        // Assert
        Assertions.assertNotNull(created);
    }



    /*
    Eu fiz com esse
    metodo primeiro, mas por conta das classes abstratas ele dava erro nos teste
    Dessa forma usei o ReflectionTestUtils, só que caso troque o nome do campo, se trocar o teste quebra

    private static boolean getFields(Object o, Object value) throws IllegalAccessException {
        Class clazz = o.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for(Field f: fields){
            f.setAccessible(true);//necessario para campos privados - dava erro no UpdateRestaurantCategoryUseCase por conta do validator
            if(f.get(o) == value){
                return true;
            } else {
                Assertions.fail(o.getClass().getSimpleName()
                        + " não guardou a dependência recebida: " + value);
            }
        }
        return false;
    } */
}
