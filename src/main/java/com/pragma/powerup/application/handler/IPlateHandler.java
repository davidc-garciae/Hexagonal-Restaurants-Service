package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.PlateCreateRequestDto;
import com.pragma.powerup.application.dto.response.PlateResponseDto;

public interface IPlateHandler {
    PlateResponseDto create(PlateCreateRequestDto requestDto, Long ownerId);
}
