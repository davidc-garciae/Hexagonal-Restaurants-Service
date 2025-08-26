package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.PlateModel;
import com.pragma.powerup.domain.spi.IPlatePersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.PlateEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IPlateEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IPlateRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlateJpaAdapter implements IPlatePersistencePort {

    private final IPlateRepository repository;
    private final IPlateEntityMapper mapper;

    @Override
    public boolean existsByNameAndRestaurantId(String name, Long restaurantId) {
        return repository.existsByNameAndRestaurantId(name, restaurantId);
    }

    @Override
    public PlateModel save(PlateModel plate) {
        PlateEntity saved = repository.save(mapper.toEntity(plate));
        return mapper.toModel(saved);
    }

    @Override
    public PlateModel findById(Long id) {
        return repository.findById(id).map(mapper::toModel).orElse(null);
    }
}
