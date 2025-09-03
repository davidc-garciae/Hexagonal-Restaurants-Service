package com.pragma.powerup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pragma.powerup.TestDataFactory;
import com.pragma.powerup.application.dto.request.RestaurantCreateRequestDto;
import com.pragma.powerup.application.dto.response.RestaurantListItemDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;
import com.pragma.powerup.application.handler.impl.RestaurantHandler;
import com.pragma.powerup.application.mapper.IRestaurantListItemMapper;
import com.pragma.powerup.application.mapper.IRestaurantRequestMapper;
import com.pragma.powerup.application.mapper.IRestaurantResponseMapper;
import com.pragma.powerup.domain.api.IRestaurantQueryServicePort;
import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.RestaurantModel;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests unitarios para RestaurantHandler
 * Valida la lógica de aplicación para la gestión de restaurantes
 */
class RestaurantHandlerTest {

    private IRestaurantServicePort restaurantServicePort;
    private IRestaurantQueryServicePort restaurantQueryServicePort;
    private IRestaurantRequestMapper requestMapper;
    private IRestaurantResponseMapper responseMapper;
    private IRestaurantListItemMapper listItemMapper;
    private RestaurantHandler restaurantHandler;

    @BeforeEach
    void setUp() {
        restaurantServicePort = mock(IRestaurantServicePort.class);
        restaurantQueryServicePort = mock(IRestaurantQueryServicePort.class);
        requestMapper = mock(IRestaurantRequestMapper.class);
        responseMapper = mock(IRestaurantResponseMapper.class);
        listItemMapper = mock(IRestaurantListItemMapper.class);

        restaurantHandler = new RestaurantHandler(
                restaurantServicePort,
                restaurantQueryServicePort,
                requestMapper,
                responseMapper,
                listItemMapper);
    }

    @Nested
    @DisplayName("Create Restaurant Tests")
    class CreateRestaurantTests {

        @Test
        @DisplayName("Should create restaurant successfully")
        void shouldCreateRestaurantSuccessfully() {
            // Given
            RestaurantCreateRequestDto requestDto = TestDataFactory.createValidRestaurantCreateRequest();
            RestaurantModel restaurantModel = TestDataFactory.createValidRestaurantModel();
            RestaurantResponseDto expectedResponse = TestDataFactory.createValidRestaurantResponse();

            when(requestMapper.toModel(requestDto)).thenReturn(restaurantModel);
            when(restaurantServicePort.createRestaurant(restaurantModel)).thenReturn(restaurantModel);
            when(responseMapper.toDto(restaurantModel)).thenReturn(expectedResponse);

            // When
            RestaurantResponseDto result = restaurantHandler.create(requestDto);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Pizza Palace");
            assertThat(result.getNit()).isEqualTo("123456789");
            assertThat(result.getOwnerId()).isEqualTo(100L);

            verify(requestMapper).toModel(requestDto);
            verify(restaurantServicePort).createRestaurant(restaurantModel);
            verify(responseMapper).toDto(restaurantModel);
        }

        @Test
        @DisplayName("Should propagate domain exception when creation fails")
        void shouldPropagateDomainExceptionWhenCreationFails() {
            // Given
            RestaurantCreateRequestDto requestDto = TestDataFactory.createValidRestaurantCreateRequest();
            RestaurantModel restaurantModel = TestDataFactory.createValidRestaurantModel();

            when(requestMapper.toModel(requestDto)).thenReturn(restaurantModel);
            when(restaurantServicePort.createRestaurant(restaurantModel))
                    .thenThrow(new DomainException("NIT already exists"));

            // When & Then
            assertThatThrownBy(() -> restaurantHandler.create(requestDto))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("NIT already exists");

            verify(requestMapper).toModel(requestDto);
            verify(restaurantServicePort).createRestaurant(restaurantModel);
        }
    }

    @Nested
    @DisplayName("List Restaurants Tests")
    class ListRestaurantsTests {

        @Test
        @DisplayName("Should list restaurants successfully")
        void shouldListRestaurantsSuccessfully() {
            // Given
            List<RestaurantModel> restaurantModels = List.of(
                    TestDataFactory.createValidRestaurantModel(),
                    TestDataFactory.createRestaurantModel(2L, "Burger House", "987654321",
                            "456 Oak St", "+0987654321", "https://logo.example.com/burger.png", 101L));
            List<RestaurantListItemDto> expectedResponse = TestDataFactory.createRestaurantListItems();

            when(restaurantQueryServicePort.listRestaurants(0, 10)).thenReturn(restaurantModels);
            when(listItemMapper.toDtoList(restaurantModels)).thenReturn(expectedResponse);

            // When
            List<RestaurantListItemDto> result = restaurantHandler.list(0, 10);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getName()).isEqualTo("Pizza Palace");
            assertThat(result.get(1).getName()).isEqualTo("Burger House");

            verify(restaurantQueryServicePort).listRestaurants(0, 10);
            verify(listItemMapper).toDtoList(restaurantModels);
        }

        @Test
        @DisplayName("Should handle empty restaurant list")
        void shouldHandleEmptyRestaurantList() {
            // Given
            List<RestaurantModel> emptyList = List.of();
            List<RestaurantListItemDto> emptyResponse = List.of();

            when(restaurantQueryServicePort.listRestaurants(0, 10)).thenReturn(emptyList);
            when(listItemMapper.toDtoList(emptyList)).thenReturn(emptyResponse);

            // When
            List<RestaurantListItemDto> result = restaurantHandler.list(0, 10);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).isEmpty();

            verify(restaurantQueryServicePort).listRestaurants(0, 10);
            verify(listItemMapper).toDtoList(emptyList);
        }

        @Test
        @DisplayName("Should handle different page parameters")
        void shouldHandleDifferentPageParameters() {
            // Given
            List<RestaurantModel> restaurantModels = List.of(TestDataFactory.createValidRestaurantModel());
            List<RestaurantListItemDto> expectedResponse = List.of(TestDataFactory.createValidRestaurantListItem());

            when(restaurantQueryServicePort.listRestaurants(2, 5)).thenReturn(restaurantModels);
            when(listItemMapper.toDtoList(restaurantModels)).thenReturn(expectedResponse);

            // When
            List<RestaurantListItemDto> result = restaurantHandler.list(2, 5);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);

            verify(restaurantQueryServicePort).listRestaurants(2, 5);
            verify(listItemMapper).toDtoList(restaurantModels);
        }
    }

    @Nested
    @DisplayName("Find Restaurant by ID Tests")
    class FindRestaurantByIdTests {

        @Test
        @DisplayName("Should find restaurant by ID successfully")
        void shouldFindRestaurantByIdSuccessfully() {
            // Given
            RestaurantModel restaurantModel = TestDataFactory.createValidRestaurantModel();
            RestaurantResponseDto expectedResponse = TestDataFactory.createValidRestaurantResponse();

            when(restaurantQueryServicePort.findById(TestDataFactory.Constants.VALID_RESTAURANT_ID))
                    .thenReturn(restaurantModel);
            when(responseMapper.toDto(restaurantModel)).thenReturn(expectedResponse);

            // When
            RestaurantResponseDto result = restaurantHandler.findById(TestDataFactory.Constants.VALID_RESTAURANT_ID);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo("Pizza Palace");

            verify(restaurantQueryServicePort).findById(TestDataFactory.Constants.VALID_RESTAURANT_ID);
            verify(responseMapper).toDto(restaurantModel);
        }

        @Test
        @DisplayName("Should throw exception when restaurant not found")
        void shouldThrowExceptionWhenRestaurantNotFound() {
            // Given
            when(restaurantQueryServicePort.findById(TestDataFactory.Constants.INVALID_RESTAURANT_ID))
                    .thenReturn(null);

            // When & Then
            assertThatThrownBy(() -> restaurantHandler.findById(TestDataFactory.Constants.INVALID_RESTAURANT_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Restaurant not found with id: " + TestDataFactory.Constants.INVALID_RESTAURANT_ID);

            verify(restaurantQueryServicePort).findById(TestDataFactory.Constants.INVALID_RESTAURANT_ID);
        }
    }
}
