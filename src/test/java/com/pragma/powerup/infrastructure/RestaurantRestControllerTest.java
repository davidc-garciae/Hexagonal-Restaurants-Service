package com.pragma.powerup.infrastructure;

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
import com.pragma.powerup.application.handler.IRestaurantHandler;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.infrastructure.input.rest.RestaurantRestController;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Tests unitarios para RestaurantRestController
 * Valida la lógica del controlador REST para restaurantes
 */
class RestaurantRestControllerTest {

    private IRestaurantHandler restaurantHandler;
    private RestaurantRestController restController;

    @BeforeEach
    void setUp() {
        restaurantHandler = mock(IRestaurantHandler.class);
        restController = new RestaurantRestController(restaurantHandler);
    }

    @Nested
    @DisplayName("Create Restaurant Tests")
    class CreateRestaurantTests {

        @Test
        @DisplayName("Should create restaurant and return 201 Created")
        void shouldCreateRestaurantAndReturn201Created() {
            // Given
            RestaurantCreateRequestDto requestDto = TestDataFactory.createValidRestaurantCreateRequest();
            RestaurantResponseDto responseDto = TestDataFactory.createValidRestaurantResponse();
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

            when(restaurantHandler.create(requestDto)).thenReturn(responseDto);

            // When
            ResponseEntity<RestaurantResponseDto> response = restController.create(requestDto, uriBuilder);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getName()).isEqualTo("Pizza Palace");
            assertThat(response.getBody().getNit()).isEqualTo("123456789");
            assertThat(response.getHeaders().getLocation()).isNotNull();

            verify(restaurantHandler).create(requestDto);
        }

        @Test
        @DisplayName("Should propagate domain exception when creation fails")
        void shouldPropagateDomainExceptionWhenCreationFails() {
            // Given
            RestaurantCreateRequestDto requestDto = TestDataFactory.createValidRestaurantCreateRequest();
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

            when(restaurantHandler.create(requestDto)).thenThrow(new DomainException("NIT already exists"));

            // When & Then
            assertThatThrownBy(() -> restController.create(requestDto, uriBuilder))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("NIT already exists");

            verify(restaurantHandler).create(requestDto);
        }
    }

    @Nested
    @DisplayName("List Restaurants Tests")
    class ListRestaurantsTests {

        @Test
        @DisplayName("Should list restaurants and return 200 OK")
        void shouldListRestaurantsAndReturn200OK() {
            // Given
            List<RestaurantListItemDto> restaurantList = TestDataFactory.createRestaurantListItems();

            when(restaurantHandler.list(0, 10)).thenReturn(restaurantList);

            // When
            ResponseEntity<List<RestaurantListItemDto>> response = restController.list(0, 10);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody()).hasSize(2);
            assertThat(response.getBody().get(0).getName()).isEqualTo("Pizza Palace");
            assertThat(response.getBody().get(1).getName()).isEqualTo("Burger House");

            verify(restaurantHandler).list(0, 10);
        }

        @Test
        @DisplayName("Should handle empty list and return 200 OK")
        void shouldHandleEmptyListAndReturn200OK() {
            // Given
            when(restaurantHandler.list(0, 10)).thenReturn(List.of());

            // When
            ResponseEntity<List<RestaurantListItemDto>> response = restController.list(0, 10);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody()).isEmpty();

            verify(restaurantHandler).list(0, 10);
        }

        @Test
        @DisplayName("Should use default pagination parameters")
        void shouldUseDefaultPaginationParameters() {
            // Given
            List<RestaurantListItemDto> restaurantList = TestDataFactory.createRestaurantListItems();

            when(restaurantHandler.list(0, 10)).thenReturn(restaurantList);

            // When - Simulating default parameters
            ResponseEntity<List<RestaurantListItemDto>> response = restController.list(0, 10);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();

            verify(restaurantHandler).list(0, 10);
        }
    }

    @Nested
    @DisplayName("Find Restaurant by ID Tests")
    class FindRestaurantByIdTests {

        @Test
        @DisplayName("Should find restaurant by ID and return 200 OK")
        void shouldFindRestaurantByIdAndReturn200OK() {
            // Given
            RestaurantResponseDto responseDto = TestDataFactory.createValidRestaurantResponse();

            when(restaurantHandler.findById(TestDataFactory.Constants.VALID_RESTAURANT_ID))
                    .thenReturn(responseDto);

            // When
            ResponseEntity<RestaurantResponseDto> response = restController.findById(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getId()).isEqualTo(1L);
            assertThat(response.getBody().getName()).isEqualTo("Pizza Palace");

            verify(restaurantHandler).findById(TestDataFactory.Constants.VALID_RESTAURANT_ID);
        }

        @Test
        @DisplayName("Should propagate domain exception when restaurant not found")
        void shouldPropagateDomainExceptionWhenRestaurantNotFound() {
            // Given
            when(restaurantHandler.findById(TestDataFactory.Constants.INVALID_RESTAURANT_ID))
                    .thenThrow(new DomainException("Restaurant not found with id: " +
                            TestDataFactory.Constants.INVALID_RESTAURANT_ID));

            // When & Then
            assertThatThrownBy(() -> restController.findById(TestDataFactory.Constants.INVALID_RESTAURANT_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Restaurant not found with id: " + TestDataFactory.Constants.INVALID_RESTAURANT_ID);

            verify(restaurantHandler).findById(TestDataFactory.Constants.INVALID_RESTAURANT_ID);
        }
    }
}
