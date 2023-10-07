package com.fawry.couponapi.model.mapper;

import com.fawry.couponapi.entity.Consumption;
import com.fawry.couponapi.model.dto.ConsumptionDTO;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface ConsumptionMapper {
    ConsumptionDTO toDto(Consumption consumption);

    List<ConsumptionDTO> toDto(List<Consumption> consumption);

    Consumption toEntity(ConsumptionDTO consumptionDto);

    List<Consumption> toEntity(List<ConsumptionDTO> consumptionDto);
}