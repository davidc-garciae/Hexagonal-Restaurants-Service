package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.PlateCategory;
import com.pragma.powerup.domain.model.PlateModel;
import java.util.List;

public interface IPlateQueryServicePort {
  List<PlateModel> listActiveByRestaurant(
      Long restaurantId, PlateCategory category, int page, int size);
}
