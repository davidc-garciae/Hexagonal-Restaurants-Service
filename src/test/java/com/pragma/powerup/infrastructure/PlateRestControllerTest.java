package com.pragma.powerup.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pragma.powerup.TestDataFactory;
import com.pragma.powerup.application.dto.request.PlateCreateRequestDto;
import com.pragma.powerup.application.dto.request.PlateStatusUpdateRequestDto;
import com.pragma.powerup.application.dto.request.PlateUpdateRequestDto;
import com.pragma.powerup.application.dto.response.PlateResponseDto;
import com.pragma.powerup.application.handler.IPlateHandler;
import com.pragma.powerup.application.util.JwtSecurityUtils;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.PlateCategory;
import com.pragma.powerup.infrastructure.input.rest.PlateRestController;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Tests unitarios para PlateRestController
 * Valida la lógica del controlador REST para platos
 */
class PlateRestControllerTest {

    private IPlateHandler plateHandler;
    private JwtSecurityUtils jwtSecurityUtils;
    private PlateRestController restController;

    @BeforeEach
    void setUp() {
        plateHandler = mock(IPlateHandler.class);
        jwtSecurityUtils = mock(JwtSecurityUtils.class);
        restController = new PlateRestController(plateHandler, jwtSecurityUtils);
    }

    @Nested
    @DisplayName("Create Plate Tests")
    class CreatePlateTests {

        @Test
        @DisplayName("Should create plate and return 201 Created")
        void shouldCreatePlateAndReturn201Created() {
            // Given
            PlateCreateRequestDto requestDto = TestDataFactory.createValidPlateCreateRequest();
            PlateResponseDto responseDto = TestDataFactory.createValidPlateResponse();
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

            when(jwtSecurityUtils.getCurrentUserId()).thenReturn(TestDataFactory.Constants.VALID_OWNER_ID);
            when(plateHandler.create(requestDto, TestDataFactory.Constants.VALID_OWNER_ID))
                    .thenReturn(responseDto);

            // When
            ResponseEntity<PlateResponseDto> response = restController.create(requestDto, uriBuilder);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getHeaders().getLocation()).isNotNull();

            verify(jwtSecurityUtils).getCurrentUserId();
            verify(plateHandler).create(requestDto, TestDataFactory.Constants.VALID_OWNER_ID);
        }

        @Test
        @DisplayName("Should propagate domain exception when creation fails")
        void shouldPropagateDomainExceptionWhenCreationFails() {
            // Given
            PlateCreateRequestDto requestDto = TestDataFactory.createValidPlateCreateRequest();
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

            when(jwtSecurityUtils.getCurrentUserId()).thenReturn(TestDataFactory.Constants.VALID_OWNER_ID);
            when(plateHandler.create(requestDto, TestDataFactory.Constants.VALID_OWNER_ID))
                    .thenThrow(new DomainException("restaurant not found"));

            // When & Then
            assertThatThrownBy(() -> restController.create(requestDto, uriBuilder))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("restaurant not found");

            verify(jwtSecurityUtils).getCurrentUserId();
            verify(plateHandler).create(requestDto, TestDataFactory.Constants.VALID_OWNER_ID);
        }
    }

    @Nested
    @DisplayName("Update Plate Tests")
    class UpdatePlateTests {

        @Test
        @DisplayName("Should update plate and return 200 OK")
        void shouldUpdatePlateAndReturn200OK() {
            // Given
            PlateUpdateRequestDto requestDto = TestDataFactory.createValidPlateUpdateRequest();
            PlateResponseDto responseDto = TestDataFactory.createValidPlateResponse();

            when(jwtSecurityUtils.getCurrentUserId()).thenReturn(TestDataFactory.Constants.VALID_OWNER_ID);
            when(plateHandler.update(TestDataFactory.Constants.VALID_PLATE_ID, requestDto,
                    TestDataFactory.Constants.VALID_OWNER_ID)).thenReturn(responseDto);

            // When
            ResponseEntity<PlateResponseDto> response = restController.update(
                    TestDataFactory.Constants.VALID_PLATE_ID, requestDto);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();

            verify(jwtSecurityUtils).getCurrentUserId();
            verify(plateHandler).update(TestDataFactory.Constants.VALID_PLATE_ID, requestDto,
                    TestDataFactory.Constants.VALID_OWNER_ID);
        }

        @Test
        @DisplayName("Should propagate domain exception when update fails")
        void shouldPropagateDomainExceptionWhenUpdateFails() {
            // Given
            PlateUpdateRequestDto requestDto = TestDataFactory.createValidPlateUpdateRequest();

            when(jwtSecurityUtils.getCurrentUserId()).thenReturn(TestDataFactory.Constants.VALID_OWNER_ID);
            when(plateHandler.update(TestDataFactory.Constants.INVALID_PLATE_ID, requestDto,
                    TestDataFactory.Constants.VALID_OWNER_ID))
                    .thenThrow(new DomainException("plate not found"));

            // When & Then
            assertThatThrownBy(() -> restController.update(TestDataFactory.Constants.INVALID_PLATE_ID, requestDto))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("plate not found");

            verify(jwtSecurityUtils).getCurrentUserId();
            verify(plateHandler).update(TestDataFactory.Constants.INVALID_PLATE_ID, requestDto,
                    TestDataFactory.Constants.VALID_OWNER_ID);
        }
    }

    @Nested
    @DisplayName("Update Plate Status Tests")
    class UpdatePlateStatusTests {

        @Test
        @DisplayName("Should update plate status and return 200 OK")
        void shouldUpdatePlateStatusAndReturn200OK() {
            // Given
            PlateStatusUpdateRequestDto requestDto = TestDataFactory.createPlateStatusUpdateRequest(false);
            PlateResponseDto responseDto = TestDataFactory.createValidPlateResponse();

            when(jwtSecurityUtils.getCurrentUserId()).thenReturn(TestDataFactory.Constants.VALID_OWNER_ID);
            when(plateHandler.updateStatus(TestDataFactory.Constants.VALID_PLATE_ID, requestDto,
                    TestDataFactory.Constants.VALID_OWNER_ID)).thenReturn(responseDto);

            // When
            ResponseEntity<PlateResponseDto> response = restController.updateStatus(
                    TestDataFactory.Constants.VALID_PLATE_ID, requestDto);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();

            verify(jwtSecurityUtils).getCurrentUserId();
            verify(plateHandler).updateStatus(TestDataFactory.Constants.VALID_PLATE_ID, requestDto,
                    TestDataFactory.Constants.VALID_OWNER_ID);
        }

        @Test
        @DisplayName("Should propagate domain exception when status update fails")
        void shouldPropagateDomainExceptionWhenStatusUpdateFails() {
            // Given
            PlateStatusUpdateRequestDto requestDto = TestDataFactory.createPlateStatusUpdateRequest(false);

            when(jwtSecurityUtils.getCurrentUserId()).thenReturn(TestDataFactory.Constants.VALID_OWNER_ID);
            when(plateHandler.updateStatus(TestDataFactory.Constants.INVALID_PLATE_ID, requestDto,
                    TestDataFactory.Constants.VALID_OWNER_ID))
                    .thenThrow(new DomainException("plate not found"));

            // When & Then
            assertThatThrownBy(() -> restController.updateStatus(
                    TestDataFactory.Constants.INVALID_PLATE_ID, requestDto))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("plate not found");

            verify(jwtSecurityUtils).getCurrentUserId();
            verify(plateHandler).updateStatus(TestDataFactory.Constants.INVALID_PLATE_ID, requestDto,
                    TestDataFactory.Constants.VALID_OWNER_ID);
        }
    }

    @Nested
    @DisplayName("List Plates by Restaurant Tests")
    class ListPlatesByRestaurantTests {

        @Test
        @DisplayName("Should list plates by restaurant and return 200 OK")
        void shouldListPlatesByRestaurantAndReturn200OK() {
            // Given
            List<PlateResponseDto> plateList = TestDataFactory.createPlateResponseList();

            when(plateHandler.listByRestaurant(TestDataFactory.Constants.VALID_RESTAURANT_ID,
                    PlateCategory.PRINCIPAL, 0, 10)).thenReturn(plateList);

            // When
            ResponseEntity<List<PlateResponseDto>> response = restController.listByRestaurant(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID, PlateCategory.PRINCIPAL, 0, 10);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody()).hasSize(2);

            verify(plateHandler).listByRestaurant(TestDataFactory.Constants.VALID_RESTAURANT_ID,
                    PlateCategory.PRINCIPAL, 0, 10);
        }

        @Test
        @DisplayName("Should list plates without category filter")
        void shouldListPlatesWithoutCategoryFilter() {
            // Given
            List<PlateResponseDto> plateList = TestDataFactory.createPlateResponseList();

            when(plateHandler.listByRestaurant(TestDataFactory.Constants.VALID_RESTAURANT_ID,
                    null, 0, 10)).thenReturn(plateList);

            // When
            ResponseEntity<List<PlateResponseDto>> response = restController.listByRestaurant(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID, null, 0, 10);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody()).hasSize(2);

            verify(plateHandler).listByRestaurant(TestDataFactory.Constants.VALID_RESTAURANT_ID,
                    null, 0, 10);
        }

        @Test
        @DisplayName("Should handle empty plate list")
        void shouldHandleEmptyPlateList() {
            // Given
            when(plateHandler.listByRestaurant(TestDataFactory.Constants.INVALID_RESTAURANT_ID,
                    null, 0, 10)).thenReturn(List.of());

            // When
            ResponseEntity<List<PlateResponseDto>> response = restController.listByRestaurant(
                    TestDataFactory.Constants.INVALID_RESTAURANT_ID, null, 0, 10);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody()).isEmpty();

            verify(plateHandler).listByRestaurant(TestDataFactory.Constants.INVALID_RESTAURANT_ID,
                    null, 0, 10);
        }

        @Test
        @DisplayName("Should use default pagination parameters")
        void shouldUseDefaultPaginationParameters() {
            // Given
            List<PlateResponseDto> plateList = TestDataFactory.createPlateResponseList();

            when(plateHandler.listByRestaurant(TestDataFactory.Constants.VALID_RESTAURANT_ID,
                    null, 0, 10)).thenReturn(plateList);

            // When - Simulating default values for page=0, size=10
            ResponseEntity<List<PlateResponseDto>> response = restController.listByRestaurant(
                    TestDataFactory.Constants.VALID_RESTAURANT_ID, null, 0, 10);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();

            verify(plateHandler).listByRestaurant(TestDataFactory.Constants.VALID_RESTAURANT_ID,
                    null, 0, 10);
        }
    }
}
