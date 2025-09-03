package com.pragma.powerup.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pragma.powerup.TestDataFactory;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserServicePort;
import com.pragma.powerup.domain.usecase.RestaurantUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests unitarios para RestaurantUseCase
 * Valida la lógica de negocio del dominio para la gestión de restaurantes
 */
class RestaurantUseCaseTest {

    private IRestaurantPersistencePort restaurantPersistencePort;
    private IUserServicePort userServicePort;
    private RestaurantUseCase restaurantUseCase;

    @BeforeEach
    void setUp() {
        restaurantPersistencePort = mock(IRestaurantPersistencePort.class);
        userServicePort = mock(IUserServicePort.class);
        restaurantUseCase = new RestaurantUseCase(restaurantPersistencePort, userServicePort);
    }

    @Nested
    @DisplayName("Create Restaurant Tests")
    class CreateRestaurantTests {

        @Test
        @DisplayName("Should create restaurant successfully when all data is valid")
        void shouldCreateRestaurantSuccessfully() {
            // Given
            RestaurantModel restaurant = TestDataFactory.createValidRestaurantModel();
            when(userServicePort.isOwnerRole(TestDataFactory.Constants.VALID_OWNER_ID)).thenReturn(true);
            when(restaurantPersistencePort.existsByNit(TestDataFactory.Constants.VALID_NIT)).thenReturn(false);
            when(restaurantPersistencePort.save(restaurant)).thenReturn(restaurant);

            // When
            RestaurantModel result = restaurantUseCase.createRestaurant(restaurant);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Pizza Palace");
            assertThat(result.getNit()).isEqualTo(TestDataFactory.Constants.VALID_NIT);
            verify(userServicePort).isOwnerRole(TestDataFactory.Constants.VALID_OWNER_ID);
            verify(restaurantPersistencePort).existsByNit(TestDataFactory.Constants.VALID_NIT);
            verify(restaurantPersistencePort).save(restaurant);
        }

        @Test
        @DisplayName("Should throw exception when restaurant is null")
        void shouldThrowExceptionWhenRestaurantIsNull() {
            // When & Then
            assertThatThrownBy(() -> restaurantUseCase.createRestaurant(null))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("restaurant is required");

            verify(userServicePort, never()).isOwnerRole(any());
            verify(restaurantPersistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when user is not owner")
        void shouldThrowExceptionWhenUserIsNotOwner() {
            // Given
            RestaurantModel restaurant = TestDataFactory.createValidRestaurantModel();
            when(userServicePort.isOwnerRole(TestDataFactory.Constants.VALID_OWNER_ID)).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> restaurantUseCase.createRestaurant(restaurant))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("OWNER role required");

            verify(userServicePort).isOwnerRole(TestDataFactory.Constants.VALID_OWNER_ID);
            verify(restaurantPersistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when name is null")
        void shouldThrowExceptionWhenNameIsNull() {
            // Given
            RestaurantModel restaurant = TestDataFactory.createRestaurantModel(
                    1L, null, TestDataFactory.Constants.VALID_NIT, "123 Main St",
                    TestDataFactory.Constants.VALID_PHONE, "https://logo.example.com/test.png",
                    TestDataFactory.Constants.VALID_OWNER_ID);
            when(userServicePort.isOwnerRole(TestDataFactory.Constants.VALID_OWNER_ID)).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> restaurantUseCase.createRestaurant(restaurant))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("name cannot be only digits");

            verify(userServicePort).isOwnerRole(TestDataFactory.Constants.VALID_OWNER_ID);
            verify(restaurantPersistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when name is blank")
        void shouldThrowExceptionWhenNameIsBlank() {
            // Given
            RestaurantModel restaurant = TestDataFactory.createRestaurantModel(
                    1L, "   ", TestDataFactory.Constants.VALID_NIT, "123 Main St",
                    TestDataFactory.Constants.VALID_PHONE, "https://logo.example.com/test.png",
                    TestDataFactory.Constants.VALID_OWNER_ID);
            when(userServicePort.isOwnerRole(TestDataFactory.Constants.VALID_OWNER_ID)).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> restaurantUseCase.createRestaurant(restaurant))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("name cannot be only digits");

            verify(userServicePort).isOwnerRole(TestDataFactory.Constants.VALID_OWNER_ID);
            verify(restaurantPersistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when name contains only digits")
        void shouldThrowExceptionWhenNameContainsOnlyDigits() {
            // Given
            RestaurantModel restaurant = TestDataFactory.createInvalidNameRestaurantModel();
            when(userServicePort.isOwnerRole(TestDataFactory.Constants.VALID_OWNER_ID)).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> restaurantUseCase.createRestaurant(restaurant))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("name cannot be only digits");

            verify(userServicePort).isOwnerRole(TestDataFactory.Constants.VALID_OWNER_ID);
            verify(restaurantPersistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when phone is null")
        void shouldThrowExceptionWhenPhoneIsNull() {
            // Given
            RestaurantModel restaurant = TestDataFactory.createRestaurantModel(
                    1L, "Pizza Palace", TestDataFactory.Constants.VALID_NIT, "123 Main St",
                    null, "https://logo.example.com/test.png", TestDataFactory.Constants.VALID_OWNER_ID);
            when(userServicePort.isOwnerRole(TestDataFactory.Constants.VALID_OWNER_ID)).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> restaurantUseCase.createRestaurant(restaurant))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("invalid phone");

            verify(userServicePort).isOwnerRole(TestDataFactory.Constants.VALID_OWNER_ID);
            verify(restaurantPersistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when phone is invalid")
        void shouldThrowExceptionWhenPhoneIsInvalid() {
            // Given
            RestaurantModel restaurant = TestDataFactory.createInvalidPhoneRestaurantModel();
            when(userServicePort.isOwnerRole(TestDataFactory.Constants.VALID_OWNER_ID)).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> restaurantUseCase.createRestaurant(restaurant))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("invalid phone");

            verify(userServicePort).isOwnerRole(TestDataFactory.Constants.VALID_OWNER_ID);
            verify(restaurantPersistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when NIT already exists")
        void shouldThrowExceptionWhenNitAlreadyExists() {
            // Given
            RestaurantModel restaurant = TestDataFactory.createValidRestaurantModel();
            when(userServicePort.isOwnerRole(TestDataFactory.Constants.VALID_OWNER_ID)).thenReturn(true);
            when(restaurantPersistencePort.existsByNit(TestDataFactory.Constants.VALID_NIT)).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> restaurantUseCase.createRestaurant(restaurant))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("NIT already exists");

            verify(userServicePort).isOwnerRole(TestDataFactory.Constants.VALID_OWNER_ID);
            verify(restaurantPersistencePort).existsByNit(TestDataFactory.Constants.VALID_NIT);
            verify(restaurantPersistencePort, never()).save(any());
        }
    }
}
