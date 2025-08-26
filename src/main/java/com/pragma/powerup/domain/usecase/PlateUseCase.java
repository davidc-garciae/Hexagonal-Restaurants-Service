package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IPlateServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.PlateModel;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IPlatePersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantQueryPort;

public class PlateUseCase implements IPlateServicePort {

    private final IPlatePersistencePort platePersistencePort;
    private final IRestaurantQueryPort restaurantQueryPort;

    public PlateUseCase(
            IPlatePersistencePort platePersistencePort, IRestaurantQueryPort restaurantQueryPort) {
        this.platePersistencePort = platePersistencePort;
        this.restaurantQueryPort = restaurantQueryPort;
    }

    @Override
    public PlateModel createPlate(PlateModel plate, Long ownerId) {
        if (plate == null) {
            throw new DomainException("plate is required");
        }
        if (plate.getName() == null || plate.getName().isBlank()) {
            throw new DomainException("name is required");
        }
        if (plate.getPrice() == null || plate.getPrice() <= 0) {
            throw new DomainException("price must be a positive integer");
        }
        if (plate.getDescription() == null || plate.getDescription().isBlank()) {
            throw new DomainException("description is required");
        }
        if (plate.getImageUrl() == null || plate.getImageUrl().isBlank()) {
            throw new DomainException("imageUrl is required");
        }
        if (plate.getCategory() == null) {
            throw new DomainException("category is required");
        }
        if (plate.getRestaurantId() == null) {
            throw new DomainException("restaurantId is required");
        }

        RestaurantModel restaurant = restaurantQueryPort.findById(plate.getRestaurantId());
        if (restaurant == null) {
            throw new DomainException("restaurant not found");
        }
        if (!restaurant.getOwnerId().equals(ownerId)) {
            throw new DomainException("only the restaurant owner can create plates");
        }

        if (platePersistencePort.existsByNameAndRestaurantId(
                plate.getName(), plate.getRestaurantId())) {
            throw new DomainException("plate name already exists in restaurant");
        }

        plate.setActive(true);
        return platePersistencePort.save(plate);
    }

    @Override
    public PlateModel updatePlate(Long plateId, Integer price, String description, Long ownerId) {
        if (plateId == null) {
            throw new DomainException("plateId is required");
        }
        PlateModel existing = platePersistencePort.findById(plateId);
        if (existing == null) {
            throw new DomainException("plate not found");
        }

        RestaurantModel restaurant = restaurantQueryPort.findById(existing.getRestaurantId());
        if (restaurant == null) {
            throw new DomainException("restaurant not found");
        }
        if (!restaurant.getOwnerId().equals(ownerId)) {
            throw new DomainException("only the restaurant owner can update plates");
        }

        if (price == null || price <= 0) {
            throw new DomainException("price must be a positive integer");
        }
        if (description == null || description.isBlank()) {
            throw new DomainException("description is required");
        }

        existing.setPrice(price);
        existing.setDescription(description);

        return platePersistencePort.save(existing);
    }
}
