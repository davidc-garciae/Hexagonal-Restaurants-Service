package com.pragma.powerup.infrastructure.out.jpa.mapper;

import com.pragma.powerup.domain.model.PlateModel;
import com.pragma.powerup.infrastructure.out.jpa.entity.PlateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPlateEntityMapper {
  PlateEntity toEntity(PlateModel model);

  PlateModel toModel(PlateEntity entity);
}
