package com.pragma.powerup.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pragma.powerup.TestDataFactory;
import com.pragma.powerup.domain.model.PlateCategory;
import com.pragma.powerup.domain.model.PlateModel;
import com.pragma.powerup.domain.spi.IPlateQueryPort;
import com.pragma.powerup.domain.usecase.PlateQueryUseCase;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests unitarios para PlateQueryUseCase
 * Valida la lógica de consulta de platos del dominio
 */
class PlateQueryUseCaseTest {

    private IPlateQueryPort plateQueryPort;
    private PlateQueryUseCase plateQueryUseCase;

    @BeforeEach
    void setUp() {
        plateQueryPort = mock(IPlateQueryPort.class);
        plateQueryUseCase = new PlateQueryUseCase(plateQueryPort);
    }

    @Nested
    @DisplayName("List Active Plates by Restaurant Tests")
    class ListActivePlatesByRestaurantTests {

        @Test
        @DisplayName("Should list active plates successfully with valid parameters")
        void shouldListActivePlatesSuccessfully() {
            // Given
            List<PlateModel> expectedPlates = TestDataFactory.createPlateModelList();
            when(plateQueryPort.findActiveByRestaurant(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID, PlateCategory.PRINCIPAL, 0, 10))
                    .thenReturn(expectedPlates);

            // When
            List<PlateModel> result = plateQueryUseCase.listActiveByRestaurant(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID, PlateCategory.PRINCIPAL, 0, 10);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(3);
            assertThat(result.get(0).getCategory()).isEqualTo(PlateCategory.ENTRADA);
            assertThat(result.get(1).getCategory()).isEqualTo(PlateCategory.PRINCIPAL);
            assertThat(result.get(2).getCategory()).isEqualTo(PlateCategory.POSTRE);
            verify(plateQueryPort).findActiveByRestaurant(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID, PlateCategory.PRINCIPAL, 0, 10);
        }

        @Test
        @DisplayName("Should list all categories when category is null")
        void shouldListAllCategoriesWhenCategoryIsNull() {
            // Given
            List<PlateModel> expectedPlates = TestDataFactory.createPlateModelList();
            when(plateQueryPort.findActiveByRestaurant(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID, null, 0, 10))
                    .thenReturn(expectedPlates);

            // When
            List<PlateModel> result = plateQueryUseCase.listActiveByRestaurant(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID, null, 0, 10);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(3);
            verify(plateQueryPort).findActiveByRestaurant(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID, null, 0, 10);
        }

        @Test
        @DisplayName("Should normalize negative page to zero")
        void shouldNormalizeNegativePageToZero() {
            // Given
            List<PlateModel> expectedPlates = TestDataFactory.createPlateModelList();
            when(plateQueryPort.findActiveByRestaurant(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID, null, 0, 10))
                    .thenReturn(expectedPlates);

            // When
            List<PlateModel> result = plateQueryUseCase.listActiveByRestaurant(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID, null, -5, 10);

            // Then
            assertThat(result).isNotNull();
            verify(plateQueryPort).findActiveByRestaurant(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID, null, 0, 10);
        }

        @Test
        @DisplayName("Should normalize zero size to one")
        void shouldNormalizeZeroSizeToOne() {
            // Given
            List<PlateModel> expectedPlates = TestDataFactory.createPlateModelList();
            when(plateQueryPort.findActiveByRestaurant(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID, null, 0, 1))
                    .thenReturn(expectedPlates);

            // When
            List<PlateModel> result = plateQueryUseCase.listActiveByRestaurant(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID, null, 0, 0);

            // Then
            assertThat(result).isNotNull();
            verify(plateQueryPort).findActiveByRestaurant(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID, null, 0, 1);
        }

        @Test
        @DisplayName("Should normalize negative size to one")
        void shouldNormalizeNegativeSizeToOne() {
            // Given
            List<PlateModel> expectedPlates = TestDataFactory.createPlateModelList();
            when(plateQueryPort.findActiveByRestaurant(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID, null, 0, 1))
                    .thenReturn(expectedPlates);

            // When
            List<PlateModel> result = plateQueryUseCase.listActiveByRestaurant(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID, null, 0, -3);

            // Then
            assertThat(result).isNotNull();
            verify(plateQueryPort).findActiveByRestaurant(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID, null, 0, 1);
        }

        @Test
        @DisplayName("Should handle empty result list")
        void shouldHandleEmptyResultList() {
            // Given
            when(plateQueryPort.findActiveByRestaurant(
                    TestDataFactory.Constants.INVALID_RESTAURANT_ID, null, 0, 10))
                    .thenReturn(List.of());

            // When
            List<PlateModel> result = plateQueryUseCase.listActiveByRestaurant(
                    TestDataFactory.Constants.INVALID_RESTAURANT_ID, null, 0, 10);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).isEmpty();
            verify(plateQueryPort).findActiveByRestaurant(
                    TestDataFactory.Constants.INVALID_RESTAURANT_ID, null, 0, 10);
        }

        @Test
        @DisplayName("Should work with different plate categories")
        void shouldWorkWithDifferentPlateCategories() {
            // Given
            PlateModel entradaPlate = TestDataFactory.createPlateModel(
                    1L, "Caesar Salad", 8000, "Fresh salad",
                    "https://images.example.com/caesar.jpg", PlateCategory.ENTRADA, true, 1L);

            when(plateQueryPort.findActiveByRestaurant(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID, PlateCategory.ENTRADA, 0, 10))
                    .thenReturn(List.of(entradaPlate));

            // When
            List<PlateModel> result = plateQueryUseCase.listActiveByRestaurant(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID, PlateCategory.ENTRADA, 0, 10);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getCategory()).isEqualTo(PlateCategory.ENTRADA);
            verify(plateQueryPort).findActiveByRestaurant(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID, PlateCategory.ENTRADA, 0, 10);
        }
    }
}
