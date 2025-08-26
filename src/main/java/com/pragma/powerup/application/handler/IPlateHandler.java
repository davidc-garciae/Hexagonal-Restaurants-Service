package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.PlateCreateRequestDto;
import com.pragma.powerup.application.dto.request.PlateStatusUpdateRequestDto;
import com.pragma.powerup.application.dto.request.PlateUpdateRequestDto;
import com.pragma.powerup.application.dto.response.PlateResponseDto;

public interface IPlateHandler {
  PlateResponseDto create(PlateCreateRequestDto requestDto, Long ownerId);

  PlateResponseDto update(Long plateId, PlateUpdateRequestDto requestDto, Long ownerId);

  PlateResponseDto updateStatus(Long plateId, PlateStatusUpdateRequestDto requestDto, Long ownerId);
}
