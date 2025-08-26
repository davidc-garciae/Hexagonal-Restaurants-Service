package com.pragma.powerup.domain.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.pragma.powerup.domain.api.IPlateServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.PlateCategory;
import com.pragma.powerup.domain.model.PlateModel;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IPlatePersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantQueryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreatePlateUseCaseTest {

  private final IPlatePersistencePort platePersistencePort = mock(IPlatePersistencePort.class);
  private final IRestaurantQueryPort restaurantQueryPort = mock(IRestaurantQueryPort.class);

  private final IPlateServicePort useCase =
      new PlateUseCase(platePersistencePort, restaurantQueryPort);

  @Test
  @DisplayName("Should create plate when data is valid and owner matches")
  void shouldCreatePlateWhenValid() {
    PlateModel toCreate =
        new PlateModel(
            null,
            "Hamburguesa",
            15000,
            "Clásica",
            "http://image.png",
            PlateCategory.PRINCIPAL,
            null,
            5L);

    when(restaurantQueryPort.findById(5L))
        .thenReturn(new RestaurantModel(5L, "R", "1", "A", "+1", "l", 10L));
    when(platePersistencePort.existsByNameAndRestaurantId("Hamburguesa", 5L)).thenReturn(false);
    when(platePersistencePort.save(any()))
        .thenAnswer(
            inv -> {
              PlateModel m = inv.getArgument(0);
              m.setId(1L);
              return m;
            });

    PlateModel created = useCase.createPlate(toCreate, 10L);

    assertThat(created.getId()).isEqualTo(1L);
    assertThat(created.isActive()).isTrue();
    assertThat(created.getRestaurantId()).isEqualTo(5L);
  }

  @Test
  @DisplayName("Should fail when price <= 0")
  void shouldFailWhenInvalidPrice() {
    PlateModel invalid =
        new PlateModel(null, "A", 0, "d", "http://img", PlateCategory.ENTRADA, null, 5L);
    assertThatThrownBy(() -> useCase.createPlate(invalid, 10L))
        .isInstanceOf(DomainException.class)
        .hasMessageContaining("price must be a positive integer");
  }

  @Test
  @DisplayName("Should fail when restaurant not found")
  void shouldFailWhenRestaurantNotFound() {
    PlateModel toCreate =
        new PlateModel(null, "A", 10, "d", "http://img", PlateCategory.ENTRADA, null, 99L);
    when(restaurantQueryPort.findById(99L)).thenReturn(null);

    assertThatThrownBy(() -> useCase.createPlate(toCreate, 10L))
        .isInstanceOf(DomainException.class)
        .hasMessageContaining("restaurant not found");
  }

  @Test
  @DisplayName("Should fail when owner does not match restaurant owner")
  void shouldFailWhenOwnerDoesNotMatch() {
    PlateModel toCreate =
        new PlateModel(null, "A", 10, "d", "http://img", PlateCategory.ENTRADA, null, 5L);
    when(restaurantQueryPort.findById(5L))
        .thenReturn(new RestaurantModel(5L, "R", "1", "A", "+1", "l", 77L));

    assertThatThrownBy(() -> useCase.createPlate(toCreate, 10L))
        .isInstanceOf(DomainException.class)
        .hasMessageContaining("only the restaurant owner can create plates");
  }

  @Test
  @DisplayName("Should fail when plate name exists in restaurant")
  void shouldFailWhenNameExists() {
    PlateModel toCreate =
        new PlateModel(null, "A", 10, "d", "http://img", PlateCategory.ENTRADA, null, 5L);
    when(restaurantQueryPort.findById(5L))
        .thenReturn(new RestaurantModel(5L, "R", "1", "A", "+1", "l", 10L));
    when(platePersistencePort.existsByNameAndRestaurantId("A", 5L)).thenReturn(true);

    assertThatThrownBy(() -> useCase.createPlate(toCreate, 10L))
        .isInstanceOf(DomainException.class)
        .hasMessageContaining("plate name already exists in restaurant");
  }
}
