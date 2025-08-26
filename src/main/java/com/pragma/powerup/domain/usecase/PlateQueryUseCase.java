package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IPlateQueryServicePort;
import com.pragma.powerup.domain.model.PlateCategory;
import com.pragma.powerup.domain.model.PlateModel;
import com.pragma.powerup.domain.spi.IPlateQueryPort;
import java.util.List;

public class PlateQueryUseCase implements IPlateQueryServicePort {

    private final IPlateQueryPort plateQueryPort;

    public PlateQueryUseCase(IPlateQueryPort plateQueryPort) {
        this.plateQueryPort = plateQueryPort;
    }

    @Override
    public List<PlateModel> listActiveByRestaurant(
            Long restaurantId, PlateCategory category, int page, int size) {
        int p = Math.max(page, 0);
        int s = Math.max(size, 1);
        return plateQueryPort.findActiveByRestaurant(restaurantId, category, p, s);
    }
}
