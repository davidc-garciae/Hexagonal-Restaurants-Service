package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IRestaurantQueryServicePort;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IRestaurantQueryPort;
import java.util.List;

public class RestaurantQueryUseCase implements IRestaurantQueryServicePort {

    private final IRestaurantQueryPort restaurantQueryPort;

    public RestaurantQueryUseCase(IRestaurantQueryPort restaurantQueryPort) {
        this.restaurantQueryPort = restaurantQueryPort;
    }

    @Override
    public List<RestaurantModel> listRestaurants(int page, int size) {
        int p = Math.max(page, 0);
        int s = Math.max(size, 1);
        return restaurantQueryPort.findAllOrderedByName(p, s);
    }
}
