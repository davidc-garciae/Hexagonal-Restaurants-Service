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

class TogglePlateStatusUseCaseTest {

  private final IPlatePersistencePort platePersistencePort = mock(IPlatePersistencePort.class);
  private final IRestaurantQueryPort restaurantQueryPort = mock(IRestaurantQueryPort.class);

  private final IPlateServicePort useCase =
      new PlateUseCase(platePersistencePort, restaurantQueryPort);

  @Test
  @DisplayName("Should set active when owner matches")
  void shouldSetActiveWhenOwnerMatches() {
    PlateModel existing =
        new PlateModel(1L, "A", 10, "d", "http://img", PlateCategory.ENTRADA, true, 5L);
    when(platePersistencePort.findById(1L)).thenReturn(existing);
    when(restaurantQueryPort.findById(5L))
        .thenReturn(new RestaurantModel(5L, "R", "1", "A", "+1", "l", 10L));
    when(platePersistencePort.save(any())).thenAnswer(inv -> inv.getArgument(0));

    PlateModel updated = useCase.setPlateActive(1L, false, 10L);

    assertThat(updated.isActive()).isFalse();
  }

  @Test
  @DisplayName("Should fail when plate not found")
  void shouldFailWhenPlateNotFound() {
    when(platePersistencePort.findById(99L)).thenReturn(null);

    assertThatThrownBy(() -> useCase.setPlateActive(99L, true, 10L))
        .isInstanceOf(DomainException.class)
        .hasMessageContaining("plate not found");
  }

  @Test
  @DisplayName("Should fail when owner mismatch")
  void shouldFailWhenOwnerMismatch() {
    PlateModel existing =
        new PlateModel(1L, "A", 10, "d", "http://img", PlateCategory.ENTRADA, true, 5L);
    when(platePersistencePort.findById(1L)).thenReturn(existing);
    when(restaurantQueryPort.findById(5L))
        .thenReturn(new RestaurantModel(5L, "R", "1", "A", "+1", "l", 77L));

    assertThatThrownBy(() -> useCase.setPlateActive(1L, false, 10L))
        .isInstanceOf(DomainException.class)
        .hasMessageContaining("only the restaurant owner can update plates");
  }
}
