package com.pragma.powerup.infrastructure.out.jpa.repository;

import com.pragma.powerup.domain.model.PlateCategory;
import com.pragma.powerup.infrastructure.out.jpa.entity.PlateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPlateRepository extends JpaRepository<PlateEntity, Long> {
  boolean existsByNameAndRestaurantId(String name, Long restaurantId);

  Page<PlateEntity> findByRestaurantIdAndActiveTrue(Long restaurantId, Pageable pageable);

  Page<PlateEntity> findByRestaurantIdAndActiveTrueAndCategory(
      Long restaurantId, PlateCategory category, Pageable pageable);
}
