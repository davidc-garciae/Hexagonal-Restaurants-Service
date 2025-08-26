package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.RestaurantModel;

public interface IRestaurantQueryPort {
    RestaurantModel findById(Long restaurantId);
}
