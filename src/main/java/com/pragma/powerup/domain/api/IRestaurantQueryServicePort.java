package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.RestaurantModel;
import java.util.List;

public interface IRestaurantQueryServicePort {
    List<RestaurantModel> listRestaurants(int page, int size);
}
