package com.github.techChallenge.application.gateways.restaurantCategory;

import com.github.techChallenge.application.exceptions.RestaurantCategoryNotFoundException;
import com.github.techChallenge.application.repositories.IRestaurantCategoryRepository;
import com.github.techChallenge.domain.restaurantCategory.IRestaurantCategoryMapper;
import com.github.techChallenge.domain.restaurantCategory.RestaurantCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantCategoryGatewayTest {
    @Mock private IRestaurantCategoryRepository repository;
    @Mock private IRestaurantCategoryMapper mapper;
    private RestaurantCategoryGateway gateway;

    @BeforeEach
    void setUp() {
        gateway = new RestaurantCategoryGateway(repository, mapper);
    }

    @Test
    void shouldReturnTheCategoryCreatedByTheRepository() {
        RestaurantCategory input = RestaurantCategory.create("Italiana");
        RestaurantCategory persisted = new RestaurantCategory(8, "Italiana");
        when(repository.create(input)).thenReturn(persisted);

        assertSame(persisted, gateway.create(input));

        verify(repository).create(input);
        verifyNoInteractions(mapper);
    }

    @Test
    void shouldPreserveTheRequestedIdentityWhenUpdating() {
        RestaurantCategory input = new RestaurantCategory(8, "Japonesa");
        RestaurantCategory persisted = new RestaurantCategory(8, "Japonesa persistida");
        when(repository.update(input, 8)).thenReturn(persisted);

        assertSame(persisted, gateway.update(input, 8));

        verify(repository).update(input, 8);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldFindTheRequestedCategory() {
        RestaurantCategory persisted = new RestaurantCategory(8, "Italiana");
        when(repository.findByID(8)).thenReturn(persisted);

        assertSame(persisted, gateway.find(8));

        verify(repository).findByID(8);
    }

    @Test
    void shouldPreserveTheNotFoundException() {
        var failure = new RestaurantCategoryNotFoundException("Categoria inexistente");
        when(repository.findByID(99)).thenThrow(failure);

        assertSame(failure, assertThrows(RestaurantCategoryNotFoundException.class, () -> gateway.find(99)));
    }

    @Test
    void shouldPreserveTheRequestedPageAndItsMetadata() {
        Page<RestaurantCategory> persisted = new PageImpl<>(
                List.of(new RestaurantCategory(8, "Italiana")), PageRequest.of(2, 3), 10);
        when(repository.list(2, 3)).thenReturn(persisted);

        Page<RestaurantCategory> result = gateway.list(2, 3);

        assertSame(persisted, result);
        assertEquals(2, result.getNumber());
        assertEquals(3, result.getSize());
        assertEquals(10, result.getTotalElements());
        verify(repository).list(2, 3);
    }

    @Test
    void shouldDeleteOnlyTheRequestedCategory() {
        gateway.delete(8);

        verify(repository).delete(8);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldPreserveDeleteFailures() {
        var failure = new RestaurantCategoryNotFoundException("Categoria inexistente");
        doThrow(failure).when(repository).delete(99);

        assertSame(failure, assertThrows(RestaurantCategoryNotFoundException.class, () -> gateway.delete(99)));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void shouldCheckTitleUniquenessForCreation(boolean exists) {
        when(repository.existsByTitle("Italiana")).thenReturn(exists);

        assertEquals(exists, gateway.titleExists("Italiana"));

        verify(repository).existsByTitle("Italiana");
        verifyNoMoreInteractions(repository);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void shouldExcludeTheCurrentCategoryFromTheDuplicateTitleCheck(boolean exists) {
        when(repository.existsByTitleAndIdNot("Italiana", 8)).thenReturn(exists);

        assertEquals(exists, gateway.titleExists("Italiana", 8));

        verify(repository).existsByTitleAndIdNot("Italiana", 8);
        verifyNoMoreInteractions(repository);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void shouldReportWhetherTheCategoryExists(boolean exists) {
        when(repository.existsById(8)).thenReturn(exists);

        assertEquals(exists, gateway.existById(8));

        verify(repository).existsById(8);
    }
}
