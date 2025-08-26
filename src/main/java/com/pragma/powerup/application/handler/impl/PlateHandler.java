package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.PlateCreateRequestDto;
import com.pragma.powerup.application.dto.request.PlateUpdateRequestDto;
import com.pragma.powerup.application.dto.response.PlateResponseDto;
import com.pragma.powerup.application.handler.IPlateHandler;
import com.pragma.powerup.application.mapper.IPlateRequestMapper;
import com.pragma.powerup.application.mapper.IPlateResponseMapper;
import com.pragma.powerup.domain.api.IPlateServicePort;
import com.pragma.powerup.domain.model.PlateModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlateHandler implements IPlateHandler {

    private final IPlateServicePort plateServicePort;
    private final IPlateRequestMapper requestMapper;
    private final IPlateResponseMapper responseMapper;

    @Override
    public PlateResponseDto create(PlateCreateRequestDto requestDto, Long ownerId) {
        PlateModel model = requestMapper.toModel(requestDto);
        return responseMapper.toDto(plateServicePort.createPlate(model, ownerId));
    }

    @Override
    public PlateResponseDto update(Long plateId, PlateUpdateRequestDto requestDto, Long ownerId) {
        PlateModel updated = plateServicePort.updatePlate(
                plateId, requestDto.getPrice(), requestDto.getDescription(), ownerId);
        return responseMapper.toDto(updated);
    }
}
