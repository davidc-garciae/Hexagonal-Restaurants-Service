package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.response.RestaurantListItemDto;
import com.pragma.powerup.domain.model.RestaurantModel;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantListItemMapper {

  @Mapping(target = "name", source = "name")
  @Mapping(target = "logoUrl", source = "logoUrl")
  RestaurantListItemDto toDto(RestaurantModel model);

  List<RestaurantListItemDto> toDtoList(List<RestaurantModel> models);
}
