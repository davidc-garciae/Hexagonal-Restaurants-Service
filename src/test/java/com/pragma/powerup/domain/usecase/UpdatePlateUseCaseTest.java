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

class UpdatePlateUseCaseTest {

    private final IPlatePersistencePort platePersistencePort = mock(IPlatePersistencePort.class);
    private final IRestaurantQueryPort restaurantQueryPort = mock(IRestaurantQueryPort.class);

    private final IPlateServicePort useCase = new PlateUseCase(platePersistencePort, restaurantQueryPort);

    @Test
    @DisplayName("Should update plate when data is valid and owner matches")
    void shouldUpdatePlateWhenValid() {
        PlateModel existing = new PlateModel(1L, "A", 10, "old", "http://img", PlateCategory.ENTRADA, true, 5L);

        when(platePersistencePort.findById(1L)).thenReturn(existing);
        when(restaurantQueryPort.findById(5L))
                .thenReturn(new RestaurantModel(5L, "R", "1", "A", "+1", "l", 10L));
        when(platePersistencePort.save(any()))
                .thenAnswer(
                        inv -> {
                            PlateModel m = inv.getArgument(0);
                            return m;
                        });

        PlateModel updated = useCase.updatePlate(1L, 25, "new desc", 10L);

        assertThat(updated.getPrice()).isEqualTo(25);
        assertThat(updated.getDescription()).isEqualTo("new desc");
        assertThat(updated.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should fail when plate not found")
    void shouldFailWhenPlateNotFound() {
        when(platePersistencePort.findById(999L)).thenReturn(null);

        assertThatThrownBy(() -> useCase.updatePlate(999L, 20, "d", 10L))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("plate not found");
    }

    @Test
    @DisplayName("Should fail when owner does not match restaurant owner")
    void shouldFailWhenOwnerMismatch() {
        PlateModel existing = new PlateModel(1L, "A", 10, "old", "http://img", PlateCategory.ENTRADA, true, 5L);
        when(platePersistencePort.findById(1L)).thenReturn(existing);
        when(restaurantQueryPort.findById(5L))
                .thenReturn(new RestaurantModel(5L, "R", "1", "A", "+1", "l", 77L));

        assertThatThrownBy(() -> useCase.updatePlate(1L, 20, "d", 10L))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("only the restaurant owner can update plates");
    }

    @Test
    @DisplayName("Should fail when price <= 0")
    void shouldFailWhenInvalidPrice() {
        PlateModel existing = new PlateModel(1L, "A", 10, "old", "http://img", PlateCategory.ENTRADA, true, 5L);
        when(platePersistencePort.findById(1L)).thenReturn(existing);
        when(restaurantQueryPort.findById(5L))
                .thenReturn(new RestaurantModel(5L, "R", "1", "A", "+1", "l", 10L));

        assertThatThrownBy(() -> useCase.updatePlate(1L, 0, "new", 10L))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("price must be a positive integer");
    }

    @Test
    @DisplayName("Should fail when description is blank")
    void shouldFailWhenBlankDescription() {
        PlateModel existing = new PlateModel(1L, "A", 10, "old", "http://img", PlateCategory.ENTRADA, true, 5L);
        when(platePersistencePort.findById(1L)).thenReturn(existing);
        when(restaurantQueryPort.findById(5L))
                .thenReturn(new RestaurantModel(5L, "R", "1", "A", "+1", "l", 10L));

        assertThatThrownBy(() -> useCase.updatePlate(1L, 20, "  ", 10L))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("description is required");
    }
}
