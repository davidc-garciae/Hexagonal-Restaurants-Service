package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.PlateCategory;
import com.pragma.powerup.domain.model.PlateModel;
import com.pragma.powerup.domain.spi.IPlateQueryPort;
import com.pragma.powerup.infrastructure.out.jpa.entity.PlateEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IPlateEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IPlateRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
public class PlateQueryJpaAdapter implements IPlateQueryPort {

    private final IPlateRepository repository;
    private final IPlateEntityMapper mapper;

    @Override
    public List<PlateModel> findActiveByRestaurant(
            Long restaurantId, PlateCategory category, int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        List<PlateEntity> entities;
        if (category == null) {
            entities = repository.findByRestaurantIdAndActiveTrue(restaurantId, pageable).getContent();
        } else {
            entities = repository
                    .findByRestaurantIdAndActiveTrueAndCategory(restaurantId, category, pageable)
                    .getContent();
        }
        return entities.stream().map(mapper::toModel).toList();
    }
}
