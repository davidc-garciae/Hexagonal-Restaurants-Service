package com.pragma.powerup.infrastructure.out.jpa.repository;

import com.pragma.powerup.infrastructure.out.jpa.entity.PlateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPlateRepository extends JpaRepository<PlateEntity, Long> {
    boolean existsByNameAndRestaurantId(String name, Long restaurantId);
}
