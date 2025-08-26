package com.pragma.powerup.infrastructure.out.jpa.entity;

import com.pragma.powerup.domain.model.PlateCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "plate")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "description", length = 1000, nullable = false)
    private String description;

    @Column(name = "image_url", length = 255, nullable = false)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 20, nullable = false)
    private PlateCategory category;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;
}
