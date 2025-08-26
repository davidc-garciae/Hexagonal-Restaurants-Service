package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.PlateModel;

public interface IPlatePersistencePort {
  boolean existsByNameAndRestaurantId(String name, Long restaurantId);

  PlateModel save(PlateModel plate);

  PlateModel findById(Long id);
}
