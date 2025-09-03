package com.pragma.powerup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pragma.powerup.TestDataFactory;
import com.pragma.powerup.application.dto.request.PlateCreateRequestDto;
import com.pragma.powerup.application.dto.request.PlateStatusUpdateRequestDto;
import com.pragma.powerup.application.dto.request.PlateUpdateRequestDto;
import com.pragma.powerup.application.dto.response.PlateResponseDto;
import com.pragma.powerup.application.handler.impl.PlateHandler;
import com.pragma.powerup.application.mapper.IPlateRequestMapper;
import com.pragma.powerup.application.mapper.IPlateResponseMapper;
import com.pragma.powerup.domain.api.IPlateQueryServicePort;
import com.pragma.powerup.domain.api.IPlateServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.PlateCategory;
import com.pragma.powerup.domain.model.PlateModel;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests unitarios para PlateHandler
 * Valida la lógica de aplicación para la gestión de platos
 */
class PlateHandlerTest {

    private IPlateServicePort plateServicePort;
    private IPlateQueryServicePort plateQueryServicePort;
    private IPlateRequestMapper requestMapper;
    private IPlateResponseMapper responseMapper;
    private PlateHandler plateHandler;

    @BeforeEach
    void setUp() {
        plateServicePort = mock(IPlateServicePort.class);
        plateQueryServicePort = mock(IPlateQueryServicePort.class);
        requestMapper = mock(IPlateRequestMapper.class);
        responseMapper = mock(IPlateResponseMapper.class);

        plateHandler = new PlateHandler(
                plateServicePort,
                plateQueryServicePort,
                requestMapper,
                responseMapper);
    }

    @Nested
    @DisplayName("Create Plate Tests")
    class CreatePlateTests {

        @Test
        @DisplayName("Should create plate successfully")
        void shouldCreatePlateSuccessfully() {
            // Given
            PlateCreateRequestDto requestDto = TestDataFactory.createValidPlateCreateRequest();
            PlateModel plateModel = TestDataFactory.createValidPlateModel();
            PlateResponseDto expectedResponse = TestDataFactory.createValidPlateResponse();

            when(requestMapper.toModel(requestDto)).thenReturn(plateModel);
            when(plateServicePort.createPlate(plateModel, TestDataFactory.Constants.VALID_OWNER_ID))
                    .thenReturn(plateModel);
            when(responseMapper.toDto(plateModel)).thenReturn(expectedResponse);

            // When
            PlateResponseDto result = plateHandler.create(requestDto, TestDataFactory.Constants.VALID_OWNER_ID);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Margherita Pizza");
            assertThat(result.getPrice()).isEqualTo(15000);
            assertThat(result.isActive()).isTrue();

            verify(requestMapper).toModel(requestDto);
            verify(plateServicePort).createPlate(plateModel, TestDataFactory.Constants.VALID_OWNER_ID);
            verify(responseMapper).toDto(plateModel);
        }

        @Test
        @DisplayName("Should propagate domain exception when creation fails")
        void shouldPropagateDomainExceptionWhenCreationFails() {
            // Given
            PlateCreateRequestDto requestDto = TestDataFactory.createValidPlateCreateRequest();
            PlateModel plateModel = TestDataFactory.createValidPlateModel();

            when(requestMapper.toModel(requestDto)).thenReturn(plateModel);
            when(plateServicePort.createPlate(plateModel, TestDataFactory.Constants.VALID_OWNER_ID))
                    .thenThrow(new DomainException("restaurant not found"));

            // When & Then
            assertThatThrownBy(() -> plateHandler.create(requestDto, TestDataFactory.Constants.VALID_OWNER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("restaurant not found");

            verify(requestMapper).toModel(requestDto);
            verify(plateServicePort).createPlate(plateModel, TestDataFactory.Constants.VALID_OWNER_ID);
        }
    }

    @Nested
    @DisplayName("Update Plate Tests")
    class UpdatePlateTests {

        @Test
        @DisplayName("Should update plate successfully")
        void shouldUpdatePlateSuccessfully() {
            // Given
            PlateUpdateRequestDto requestDto = TestDataFactory.createValidPlateUpdateRequest();
            PlateModel updatedPlate = TestDataFactory.createValidPlateModel();
            PlateResponseDto expectedResponse = TestDataFactory.createValidPlateResponse();

            when(plateServicePort.updatePlate(
                    TestDataFactory.Constants.VALID_PLATE_ID,
                    requestDto.getPrice(),
                    requestDto.getDescription(),
                    TestDataFactory.Constants.VALID_OWNER_ID))
                    .thenReturn(updatedPlate);
            when(responseMapper.toDto(updatedPlate)).thenReturn(expectedResponse);

            // When
            PlateResponseDto result = plateHandler.update(
                    TestDataFactory.Constants.VALID_PLATE_ID, requestDto, TestDataFactory.Constants.VALID_OWNER_ID);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Margherita Pizza");

            verify(plateServicePort).updatePlate(
                    TestDataFactory.Constants.VALID_PLATE_ID,
                    requestDto.getPrice(),
                    requestDto.getDescription(),
                    TestDataFactory.Constants.VALID_OWNER_ID);
            verify(responseMapper).toDto(updatedPlate);
        }

        @Test
        @DisplayName("Should propagate domain exception when update fails")
        void shouldPropagateDomainExceptionWhenUpdateFails() {
            // Given
            PlateUpdateRequestDto requestDto = TestDataFactory.createValidPlateUpdateRequest();

            when(plateServicePort.updatePlate(
                    TestDataFactory.Constants.INVALID_PLATE_ID,
                    requestDto.getPrice(),
                    requestDto.getDescription(),
                    TestDataFactory.Constants.VALID_OWNER_ID))
                    .thenThrow(new DomainException("plate not found"));

            // When & Then
            assertThatThrownBy(() -> plateHandler.update(
                    TestDataFactory.Constants.INVALID_PLATE_ID, requestDto, TestDataFactory.Constants.VALID_OWNER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("plate not found");

            verify(plateServicePort).updatePlate(
                    TestDataFactory.Constants.INVALID_PLATE_ID,
                    requestDto.getPrice(),
                    requestDto.getDescription(),
                    TestDataFactory.Constants.VALID_OWNER_ID);
        }
    }

    @Nested
    @DisplayName("Update Plate Status Tests")
    class UpdatePlateStatusTests {

        @Test
        @DisplayName("Should update plate status successfully")
        void shouldUpdatePlateStatusSuccessfully() {
            // Given
            PlateStatusUpdateRequestDto requestDto = TestDataFactory.createPlateStatusUpdateRequest(false);
            PlateModel updatedPlate = TestDataFactory.createValidPlateModel();
            PlateResponseDto expectedResponse = TestDataFactory.createValidPlateResponse();

            when(plateServicePort.setPlateActive(
                    TestDataFactory.Constants.VALID_PLATE_ID,
                    requestDto.getActive(),
                    TestDataFactory.Constants.VALID_OWNER_ID))
                    .thenReturn(updatedPlate);
            when(responseMapper.toDto(updatedPlate)).thenReturn(expectedResponse);

            // When
            PlateResponseDto result = plateHandler.updateStatus(
                    TestDataFactory.Constants.VALID_PLATE_ID, requestDto, TestDataFactory.Constants.VALID_OWNER_ID);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Margherita Pizza");

            verify(plateServicePort).setPlateActive(
                    TestDataFactory.Constants.VALID_PLATE_ID,
                    requestDto.getActive(),
                    TestDataFactory.Constants.VALID_OWNER_ID);
            verify(responseMapper).toDto(updatedPlate);
        }

        @Test
        @DisplayName("Should propagate domain exception when status update fails")
        void shouldPropagateDomainExceptionWhenStatusUpdateFails() {
            // Given
            PlateStatusUpdateRequestDto requestDto = TestDataFactory.createPlateStatusUpdateRequest(false);

            when(plateServicePort.setPlateActive(
                    TestDataFactory.Constants.INVALID_PLATE_ID,
                    requestDto.getActive(),
                    TestDataFactory.Constants.VALID_OWNER_ID))
                    .thenThrow(new DomainException("plate not found"));

            // When & Then
            assertThatThrownBy(() -> plateHandler.updateStatus(
                    TestDataFactory.Constants.INVALID_PLATE_ID, requestDto, TestDataFactory.Constants.VALID_OWNER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("plate not found");

            verify(plateServicePort).setPlateActive(
                    TestDataFactory.Constants.INVALID_PLATE_ID,
                    requestDto.getActive(),
                    TestDataFactory.Constants.VALID_OWNER_ID);
        }
    }

    @Nested
    @DisplayName("List Plates by Restaurant Tests")
    class ListPlatesByRestaurantTests {

        @Test
        @DisplayName("Should list plates by restaurant successfully")
        void shouldListPlatesByRestaurantSuccessfully() {
            // Given
            List<PlateModel> plateModels = List.of(
                    TestDataFactory.createPlateModel(1L, "Caesar Salad", 8000, "Fresh lettuce with Caesar dressing",
                            "https://images.example.com/caesar.jpg", PlateCategory.ENTRADA, true, 1L),
                    TestDataFactory.createPlateModel(2L, "Margherita Pizza", 15000, "Classic pizza with tomato sauce",
                            "https://images.example.com/margherita.jpg", PlateCategory.PRINCIPAL, true, 1L));

            List<PlateResponseDto> expectedResponse = TestDataFactory.createPlateResponseList();

            when(plateQueryServicePort.listActiveByRestaurant(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID, PlateCategory.PRINCIPAL, 0, 10))
                    .thenReturn(plateModels);

            // Mock each model separately
            when(responseMapper.toDto(plateModels.get(0))).thenReturn(expectedResponse.get(0));
            when(responseMapper.toDto(plateModels.get(1))).thenReturn(expectedResponse.get(1));

            // When
            List<PlateResponseDto> result = plateHandler.listByRestaurant(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID, PlateCategory.PRINCIPAL, 0, 10);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(2);

            verify(plateQueryServicePort).listActiveByRestaurant(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID, PlateCategory.PRINCIPAL, 0, 10);
        }

        @Test
        @DisplayName("Should handle empty plate list")
        void shouldHandleEmptyPlateList() {
            // Given
            when(plateQueryServicePort.listActiveByRestaurant(
                    TestDataFactory.Constants.INVALID_RESTAURANT_ID, null, 0, 10))
                    .thenReturn(List.of());

            // When
            List<PlateResponseDto> result = plateHandler.listByRestaurant(
                    TestDataFactory.Constants.INVALID_RESTAURANT_ID, null, 0, 10);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).isEmpty();

            verify(plateQueryServicePort).listActiveByRestaurant(
                    TestDataFactory.Constants.INVALID_RESTAURANT_ID, null, 0, 10);
        }

        @Test
        @DisplayName("Should list plates without category filter")
        void shouldListPlatesWithoutCategoryFilter() {
            // Given
            List<PlateModel> plateModels = List.of(
                    TestDataFactory.createPlateModel(1L, "Caesar Salad", 8000, "Fresh lettuce with Caesar dressing",
                            "https://images.example.com/caesar.jpg", PlateCategory.ENTRADA, true, 1L),
                    TestDataFactory.createPlateModel(2L, "Margherita Pizza", 15000, "Classic pizza with tomato sauce",
                            "https://images.example.com/margherita.jpg", PlateCategory.PRINCIPAL, true, 1L));
            List<PlateResponseDto> expectedResponse = TestDataFactory.createPlateResponseList();

            when(plateQueryServicePort.listActiveByRestaurant(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID, null, 0, 10))
                    .thenReturn(plateModels);

            // Mock each model separately
            when(responseMapper.toDto(plateModels.get(0))).thenReturn(expectedResponse.get(0));
            when(responseMapper.toDto(plateModels.get(1))).thenReturn(expectedResponse.get(1));

            // When
            List<PlateResponseDto> result = plateHandler.listByRestaurant(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID, null, 0, 10);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(2);

            verify(plateQueryServicePort).listActiveByRestaurant(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID, null, 0, 10);
        }
    }
}
