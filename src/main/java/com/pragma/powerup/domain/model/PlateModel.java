package com.pragma.powerup.domain.model;

public class PlateModel {
    private Long id;
    private String name;
    private Integer price;
    private String description;
    private String imageUrl;
    private PlateCategory category;
    private boolean active;
    private Long restaurantId;

    public PlateModel() {
        this.active = true;
    }

    public PlateModel(
            Long id,
            String name,
            Integer price,
            String description,
            String imageUrl,
            PlateCategory category,
            Boolean active,
            Long restaurantId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
        this.active = active != null ? active : true;
        this.restaurantId = restaurantId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public PlateCategory getCategory() {
        return category;
    }

    public void setCategory(PlateCategory category) {
        this.category = category;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }
}
