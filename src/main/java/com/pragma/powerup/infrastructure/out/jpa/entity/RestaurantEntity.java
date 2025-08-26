package com.pragma.powerup.infrastructure.out.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "name", length = 100, nullable = false)
  private String name;

  @Column(name = "nit", length = 20, nullable = false, unique = true)
  private String nit;

  @Column(name = "address", length = 200, nullable = false)
  private String address;

  @Column(name = "phone", length = 13, nullable = false)
  private String phone;

  @Column(name = "logo_url", length = 255, nullable = false)
  private String logoUrl;

  @Column(name = "owner_id", nullable = false)
  private Long ownerId;
}
