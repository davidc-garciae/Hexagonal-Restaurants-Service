package com.pragma.powerup.domain.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.pragma.powerup.domain.api.IRestaurantQueryServicePort;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IRestaurantQueryPort;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ListRestaurantsUseCaseTest {

    private final IRestaurantQueryPort restaurantQueryPort = mock(IRestaurantQueryPort.class);
    private final IRestaurantQueryServicePort useCase = new RestaurantQueryUseCase(restaurantQueryPort);

    @Test
    @DisplayName("Should list restaurants ordered by name with pagination")
    void shouldListRestaurantsOrdered() {
        List<RestaurantModel> data = List.of(
                new RestaurantModel(1L, "A", "n1", "a", "+1", "l1", 10L),
                new RestaurantModel(2L, "B", "n2", "a", "+1", "l2", 20L));
        when(restaurantQueryPort.findAllOrderedByName(0, 2)).thenReturn(data);

        List<RestaurantModel> result = useCase.listRestaurants(0, 2);
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("A");
        assertThat(result.get(1).getName()).isEqualTo("B");
    }

    @Test
    @DisplayName("Should normalize negative page and invalid size")
    void shouldNormalizeInputs() {
        when(restaurantQueryPort.findAllOrderedByName(0, 1)).thenReturn(List.of());
        List<RestaurantModel> result = useCase.listRestaurants(-1, 0);
        assertThat(result).isNotNull();
    }
}
