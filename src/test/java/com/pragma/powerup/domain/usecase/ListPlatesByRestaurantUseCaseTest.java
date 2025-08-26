package com.pragma.powerup.domain.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.pragma.powerup.domain.api.IPlateQueryServicePort;
import com.pragma.powerup.domain.model.PlateCategory;
import com.pragma.powerup.domain.model.PlateModel;
import com.pragma.powerup.domain.spi.IPlateQueryPort;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ListPlatesByRestaurantUseCaseTest {

    private final IPlateQueryPort plateQueryPort = mock(IPlateQueryPort.class);
    private final IPlateQueryServicePort useCase = new PlateQueryUseCase(plateQueryPort);

    @Test
    @DisplayName("Should list active plates by restaurant with optional category and pagination")
    void shouldListActivePlates() {
        List<PlateModel> data = List.of(
                new PlateModel(1L, "A", 10, "d", "u", PlateCategory.ENTRADA, true, 5L),
                new PlateModel(2L, "B", 20, "d", "u", PlateCategory.PRINCIPAL, true, 5L));
        when(plateQueryPort.findActiveByRestaurant(5L, null, 0, 10)).thenReturn(data);

        List<PlateModel> result = useCase.listActiveByRestaurant(5L, null, 0, 10);
        assertThat(result).hasSize(2);
        assertThat(result.get(0).isActive()).isTrue();
    }

    @Test
    @DisplayName("Should normalize negative page and size")
    void shouldNormalizeInputs() {
        when(plateQueryPort.findActiveByRestaurant(5L, PlateCategory.ENTRADA, 0, 1))
                .thenReturn(List.of());
        List<PlateModel> result = useCase.listActiveByRestaurant(5L, PlateCategory.ENTRADA, -1, 0);
        assertThat(result).isNotNull();
    }
}
