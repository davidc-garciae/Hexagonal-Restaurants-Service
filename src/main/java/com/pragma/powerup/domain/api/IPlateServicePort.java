package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.PlateModel;

public interface IPlateServicePort {
    PlateModel createPlate(PlateModel plate, Long ownerId);
}
