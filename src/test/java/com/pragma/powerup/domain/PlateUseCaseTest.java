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
import com.pragma.powerup.domain.model.PlateCategory;
import com.pragma.powerup.domain.model.PlateModel;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IPlatePersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantQueryPort;
import com.pragma.powerup.domain.usecase.PlateUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests unitarios para PlateUseCase
 * Valida la lógica de negocio del dominio para la gestión de platos
 */
class PlateUseCaseTest {

    private IPlatePersistencePort platePersistencePort;
    private IRestaurantQueryPort restaurantQueryPort;
    private PlateUseCase plateUseCase;

    @BeforeEach
    void setUp() {
        platePersistencePort = mock(IPlatePersistencePort.class);
        restaurantQueryPort = mock(IRestaurantQueryPort.class);
        plateUseCase = new PlateUseCase(platePersistencePort, restaurantQueryPort);
    }

    @Nested
    @DisplayName("Create Plate Tests")
    class CreatePlateTests {

        @Test
        @DisplayName("Should create plate successfully when all data is valid")
        void shouldCreatePlateSuccessfully() {
            // Given
            PlateModel plate = TestDataFactory.createValidPlateModel();
            RestaurantModel restaurant = TestDataFactory.createValidRestaurantModel();

            when(restaurantQueryPort.findById(TestDataFactory.Constants.VALID_RESTAURANT_ID)).thenReturn(restaurant);
            when(platePersistencePort.existsByNameAndRestaurantId(
                    TestDataFactory.Constants.VALID_PLATE_NAME, TestDataFactory.Constants.VALID_RESTAURANT_ID))
                    .thenReturn(false);
            when(platePersistencePort.save(any(PlateModel.class))).thenReturn(plate);

            // When
            PlateModel result = plateUseCase.createPlate(plate, TestDataFactory.Constants.VALID_OWNER_ID);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Margherita Pizza");
            assertThat(result.isActive()).isTrue();
            verify(restaurantQueryPort).findById(TestDataFactory.Constants.VALID_RESTAURANT_ID);
            verify(platePersistencePort).existsByNameAndRestaurantId(
                    "Margherita Pizza", TestDataFactory.Constants.VALID_RESTAURANT_ID);
            verify(platePersistencePort).save(any(PlateModel.class));
        }

        @Test
        @DisplayName("Should throw exception when plate is null")
        void shouldThrowExceptionWhenPlateIsNull() {
            // When & Then
            assertThatThrownBy(() -> plateUseCase.createPlate(null, TestDataFactory.Constants.VALID_OWNER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("plate is required");

            verify(platePersistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when name is null")
        void shouldThrowExceptionWhenNameIsNull() {
            // Given
            PlateModel plate = TestDataFactory.createPlateModel(
                    1L, null, 15000, "Test description",
                    "https://images.example.com/test.jpg", PlateCategory.PRINCIPAL, true, 1L);

            // When & Then
            assertThatThrownBy(() -> plateUseCase.createPlate(plate, TestDataFactory.Constants.VALID_OWNER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("name is required");

            verify(platePersistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when name is blank")
        void shouldThrowExceptionWhenNameIsBlank() {
            // Given
            PlateModel plate = TestDataFactory.createPlateModel(
                    1L, "   ", 15000, "Test description",
                    "https://images.example.com/test.jpg", PlateCategory.PRINCIPAL, true, 1L);

            // When & Then
            assertThatThrownBy(() -> plateUseCase.createPlate(plate, TestDataFactory.Constants.VALID_OWNER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("name is required");

            verify(platePersistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when price is null")
        void shouldThrowExceptionWhenPriceIsNull() {
            // Given
            PlateModel plate = TestDataFactory.createPlateModel(
                    1L, "Test Plate", null, "Test description",
                    "https://images.example.com/test.jpg", PlateCategory.PRINCIPAL, true, 1L);

            // When & Then
            assertThatThrownBy(() -> plateUseCase.createPlate(plate, TestDataFactory.Constants.VALID_OWNER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("price must be a positive integer");

            verify(platePersistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when price is negative")
        void shouldThrowExceptionWhenPriceIsNegative() {
            // Given
            PlateModel plate = TestDataFactory.createInvalidPricePlateModel();

            // When & Then
            assertThatThrownBy(() -> plateUseCase.createPlate(plate, TestDataFactory.Constants.VALID_OWNER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("price must be a positive integer");

            verify(platePersistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when price is zero")
        void shouldThrowExceptionWhenPriceIsZero() {
            // Given
            PlateModel plate = TestDataFactory.createPlateModel(
                    1L, "Test Plate", 0, "Test description",
                    "https://images.example.com/test.jpg", PlateCategory.PRINCIPAL, true, 1L);

            // When & Then
            assertThatThrownBy(() -> plateUseCase.createPlate(plate, TestDataFactory.Constants.VALID_OWNER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("price must be a positive integer");

            verify(platePersistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when description is null")
        void shouldThrowExceptionWhenDescriptionIsNull() {
            // Given
            PlateModel plate = TestDataFactory.createPlateModel(
                    1L, "Test Plate", 15000, null,
                    "https://images.example.com/test.jpg", PlateCategory.PRINCIPAL, true, 1L);

            // When & Then
            assertThatThrownBy(() -> plateUseCase.createPlate(plate, TestDataFactory.Constants.VALID_OWNER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("description is required");

            verify(platePersistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when description is blank")
        void shouldThrowExceptionWhenDescriptionIsBlank() {
            // Given
            PlateModel plate = TestDataFactory.createEmptyDescriptionPlateModel();

            // When & Then
            assertThatThrownBy(() -> plateUseCase.createPlate(plate, TestDataFactory.Constants.VALID_OWNER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("description is required");

            verify(platePersistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when imageUrl is null")
        void shouldThrowExceptionWhenImageUrlIsNull() {
            // Given
            PlateModel plate = TestDataFactory.createPlateModel(
                    1L, "Test Plate", 15000, "Test description",
                    null, PlateCategory.PRINCIPAL, true, 1L);

            // When & Then
            assertThatThrownBy(() -> plateUseCase.createPlate(plate, TestDataFactory.Constants.VALID_OWNER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("imageUrl is required");

            verify(platePersistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when imageUrl is blank")
        void shouldThrowExceptionWhenImageUrlIsBlank() {
            // Given
            PlateModel plate = TestDataFactory.createPlateModel(
                    1L, "Test Plate", 15000, "Test description",
                    "   ", PlateCategory.PRINCIPAL, true, 1L);

            // When & Then
            assertThatThrownBy(() -> plateUseCase.createPlate(plate, TestDataFactory.Constants.VALID_OWNER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("imageUrl is required");

            verify(platePersistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when category is null")
        void shouldThrowExceptionWhenCategoryIsNull() {
            // Given
            PlateModel plate = TestDataFactory.createPlateModel(
                    1L, "Test Plate", 15000, "Test description",
                    "https://images.example.com/test.jpg", null, true, 1L);

            // When & Then
            assertThatThrownBy(() -> plateUseCase.createPlate(plate, TestDataFactory.Constants.VALID_OWNER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("category is required");

            verify(platePersistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when restaurantId is null")
        void shouldThrowExceptionWhenRestaurantIdIsNull() {
            // Given
            PlateModel plate = TestDataFactory.createPlateModel(
                    1L, "Test Plate", 15000, "Test description",
                    "https://images.example.com/test.jpg", PlateCategory.PRINCIPAL, true, null);

            // When & Then
            assertThatThrownBy(() -> plateUseCase.createPlate(plate, TestDataFactory.Constants.VALID_OWNER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("restaurantId is required");

            verify(platePersistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when restaurant not found")
        void shouldThrowExceptionWhenRestaurantNotFound() {
            // Given
            PlateModel plate = TestDataFactory.createValidPlateModel();
            when(restaurantQueryPort.findById(TestDataFactory.Constants.VALID_RESTAURANT_ID)).thenReturn(null);

            // When & Then
            assertThatThrownBy(() -> plateUseCase.createPlate(plate, TestDataFactory.Constants.VALID_OWNER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("restaurant not found");

            verify(restaurantQueryPort).findById(TestDataFactory.Constants.VALID_RESTAURANT_ID);
            verify(platePersistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when user is not restaurant owner")
        void shouldThrowExceptionWhenUserIsNotRestaurantOwner() {
            // Given
            PlateModel plate = TestDataFactory.createValidPlateModel();
            RestaurantModel restaurant = TestDataFactory.createValidRestaurantModel();
            when(restaurantQueryPort.findById(TestDataFactory.Constants.VALID_RESTAURANT_ID)).thenReturn(restaurant);

            // When & Then
            assertThatThrownBy(() -> plateUseCase.createPlate(plate, TestDataFactory.Constants.INVALID_OWNER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("only the restaurant owner can create plates");

            verify(restaurantQueryPort).findById(TestDataFactory.Constants.VALID_RESTAURANT_ID);
            verify(platePersistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when plate name already exists in restaurant")
        void shouldThrowExceptionWhenPlateNameAlreadyExists() {
            // Given
            PlateModel plate = TestDataFactory.createValidPlateModel();
            RestaurantModel restaurant = TestDataFactory.createValidRestaurantModel();

            when(restaurantQueryPort.findById(TestDataFactory.Constants.VALID_RESTAURANT_ID)).thenReturn(restaurant);
            when(platePersistencePort.existsByNameAndRestaurantId(
                    "Margherita Pizza", TestDataFactory.Constants.VALID_RESTAURANT_ID))
                    .thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> plateUseCase.createPlate(plate, TestDataFactory.Constants.VALID_OWNER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("plate name already exists in restaurant");

            verify(restaurantQueryPort).findById(TestDataFactory.Constants.VALID_RESTAURANT_ID);
            verify(platePersistencePort).existsByNameAndRestaurantId(
                    "Margherita Pizza", TestDataFactory.Constants.VALID_RESTAURANT_ID);
            verify(platePersistencePort, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Update Plate Tests")
    class UpdatePlateTests {

        @Test
        @DisplayName("Should update plate successfully when all data is valid")
        void shouldUpdatePlateSuccessfully() {
            // Given
            PlateModel existingPlate = TestDataFactory.createValidPlateModel();
            RestaurantModel restaurant = TestDataFactory.createValidRestaurantModel();

            when(platePersistencePort.findById(TestDataFactory.Constants.VALID_PLATE_ID)).thenReturn(existingPlate);
            when(restaurantQueryPort.findById(TestDataFactory.Constants.VALID_RESTAURANT_ID)).thenReturn(restaurant);
            when(platePersistencePort.save(any(PlateModel.class))).thenReturn(existingPlate);

            // When
            PlateModel result = plateUseCase.updatePlate(
                    TestDataFactory.Constants.VALID_PLATE_ID, 20000, "Updated description",
                    TestDataFactory.Constants.VALID_OWNER_ID);

            // Then
            assertThat(result).isNotNull();
            verify(platePersistencePort).findById(TestDataFactory.Constants.VALID_PLATE_ID);
            verify(restaurantQueryPort).findById(TestDataFactory.Constants.VALID_RESTAURANT_ID);
            verify(platePersistencePort).save(existingPlate);
        }

        @Test
        @DisplayName("Should throw exception when plateId is null")
        void shouldThrowExceptionWhenPlateIdIsNull() {
            // When & Then
            assertThatThrownBy(() -> plateUseCase.updatePlate(
                    null, 20000, "Updated description", TestDataFactory.Constants.VALID_OWNER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("plateId is required");

            verify(platePersistencePort, never()).findById(any());
        }

        @Test
        @DisplayName("Should throw exception when plate not found")
        void shouldThrowExceptionWhenPlateNotFound() {
            // Given
            when(platePersistencePort.findById(TestDataFactory.Constants.INVALID_PLATE_ID)).thenReturn(null);

            // When & Then
            assertThatThrownBy(() -> plateUseCase.updatePlate(
                    TestDataFactory.Constants.INVALID_PLATE_ID, 20000, "Updated description",
                    TestDataFactory.Constants.VALID_OWNER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("plate not found");

            verify(platePersistencePort).findById(TestDataFactory.Constants.INVALID_PLATE_ID);
            verify(platePersistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when price is null")
        void shouldThrowExceptionWhenUpdatePriceIsNull() {
            // Given
            PlateModel existingPlate = TestDataFactory.createValidPlateModel();
            RestaurantModel restaurant = TestDataFactory.createValidRestaurantModel();

            when(platePersistencePort.findById(TestDataFactory.Constants.VALID_PLATE_ID)).thenReturn(existingPlate);
            when(restaurantQueryPort.findById(TestDataFactory.Constants.VALID_RESTAURANT_ID)).thenReturn(restaurant);

            // When & Then
            assertThatThrownBy(() -> plateUseCase.updatePlate(
                    TestDataFactory.Constants.VALID_PLATE_ID, null, "Updated description",
                    TestDataFactory.Constants.VALID_OWNER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("price must be a positive integer");

            verify(platePersistencePort).findById(TestDataFactory.Constants.VALID_PLATE_ID);
            verify(restaurantQueryPort).findById(TestDataFactory.Constants.VALID_RESTAURANT_ID);
            verify(platePersistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when price is zero or negative")
        void shouldThrowExceptionWhenUpdatePriceIsZeroOrNegative() {
            // Given
            PlateModel existingPlate = TestDataFactory.createValidPlateModel();
            RestaurantModel restaurant = TestDataFactory.createValidRestaurantModel();

            when(platePersistencePort.findById(TestDataFactory.Constants.VALID_PLATE_ID)).thenReturn(existingPlate);
            when(restaurantQueryPort.findById(TestDataFactory.Constants.VALID_RESTAURANT_ID)).thenReturn(restaurant);

            // When & Then
            assertThatThrownBy(() -> plateUseCase.updatePlate(
                    TestDataFactory.Constants.VALID_PLATE_ID, 0, "Updated description",
                    TestDataFactory.Constants.VALID_OWNER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("price must be a positive integer");

            verify(platePersistencePort).findById(TestDataFactory.Constants.VALID_PLATE_ID);
            verify(restaurantQueryPort).findById(TestDataFactory.Constants.VALID_RESTAURANT_ID);
            verify(platePersistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when description is null")
        void shouldThrowExceptionWhenUpdateDescriptionIsNull() {
            // Given
            PlateModel existingPlate = TestDataFactory.createValidPlateModel();
            RestaurantModel restaurant = TestDataFactory.createValidRestaurantModel();

            when(platePersistencePort.findById(TestDataFactory.Constants.VALID_PLATE_ID)).thenReturn(existingPlate);
            when(restaurantQueryPort.findById(TestDataFactory.Constants.VALID_RESTAURANT_ID)).thenReturn(restaurant);

            // When & Then
            assertThatThrownBy(() -> plateUseCase.updatePlate(
                    TestDataFactory.Constants.VALID_PLATE_ID, 20000, null,
                    TestDataFactory.Constants.VALID_OWNER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("description is required");

            verify(platePersistencePort).findById(TestDataFactory.Constants.VALID_PLATE_ID);
            verify(restaurantQueryPort).findById(TestDataFactory.Constants.VALID_RESTAURANT_ID);
            verify(platePersistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when description is blank")
        void shouldThrowExceptionWhenUpdateDescriptionIsBlank() {
            // Given
            PlateModel existingPlate = TestDataFactory.createValidPlateModel();
            RestaurantModel restaurant = TestDataFactory.createValidRestaurantModel();

            when(platePersistencePort.findById(TestDataFactory.Constants.VALID_PLATE_ID)).thenReturn(existingPlate);
            when(restaurantQueryPort.findById(TestDataFactory.Constants.VALID_RESTAURANT_ID)).thenReturn(restaurant);

            // When & Then
            assertThatThrownBy(() -> plateUseCase.updatePlate(
                    TestDataFactory.Constants.VALID_PLATE_ID, 20000, "   ",
                    TestDataFactory.Constants.VALID_OWNER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("description is required");

            verify(platePersistencePort).findById(TestDataFactory.Constants.VALID_PLATE_ID);
            verify(restaurantQueryPort).findById(TestDataFactory.Constants.VALID_RESTAURANT_ID);
            verify(platePersistencePort, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Set Plate Active Tests")
    class SetPlateActiveTests {

        @Test
        @DisplayName("Should set plate active status successfully")
        void shouldSetPlateActiveStatusSuccessfully() {
            // Given
            PlateModel existingPlate = TestDataFactory.createValidPlateModel();
            RestaurantModel restaurant = TestDataFactory.createValidRestaurantModel();

            when(platePersistencePort.findById(TestDataFactory.Constants.VALID_PLATE_ID)).thenReturn(existingPlate);
            when(restaurantQueryPort.findById(TestDataFactory.Constants.VALID_RESTAURANT_ID)).thenReturn(restaurant);
            when(platePersistencePort.save(any(PlateModel.class))).thenReturn(existingPlate);

            // When
            PlateModel result = plateUseCase.setPlateActive(
                    TestDataFactory.Constants.VALID_PLATE_ID, false, TestDataFactory.Constants.VALID_OWNER_ID);

            // Then
            assertThat(result).isNotNull();
            verify(platePersistencePort).findById(TestDataFactory.Constants.VALID_PLATE_ID);
            verify(restaurantQueryPort).findById(TestDataFactory.Constants.VALID_RESTAURANT_ID);
            verify(platePersistencePort).save(existingPlate);
        }

        @Test
        @DisplayName("Should throw exception when plateId is null for status update")
        void shouldThrowExceptionWhenPlateIdIsNullForStatusUpdate() {
            // When & Then
            assertThatThrownBy(() -> plateUseCase.setPlateActive(
                    null, false, TestDataFactory.Constants.VALID_OWNER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("plateId is required");

            verify(platePersistencePort, never()).findById(any());
        }

        @Test
        @DisplayName("Should throw exception when plate not found for status update")
        void shouldThrowExceptionWhenPlateNotFoundForStatusUpdate() {
            // Given
            when(platePersistencePort.findById(TestDataFactory.Constants.INVALID_PLATE_ID)).thenReturn(null);

            // When & Then
            assertThatThrownBy(() -> plateUseCase.setPlateActive(
                    TestDataFactory.Constants.INVALID_PLATE_ID, false, TestDataFactory.Constants.VALID_OWNER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("plate not found");

            verify(platePersistencePort).findById(TestDataFactory.Constants.INVALID_PLATE_ID);
            verify(platePersistencePort, never()).save(any());
        }
    }
}
