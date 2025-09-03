package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.PlateCategory;
import com.pragma.powerup.domain.model.PlateModel;
import java.util.List;

public interface IPlateQueryPort {
  List<PlateModel> findActiveByRestaurant(
      Long restaurantId, PlateCategory category, int page, int size);
}
