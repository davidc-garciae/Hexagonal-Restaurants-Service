package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.RestaurantModel;

public interface IRestaurantPersistencePort {
  boolean existsByNit(String nit);

  RestaurantModel save(RestaurantModel restaurant);
}
