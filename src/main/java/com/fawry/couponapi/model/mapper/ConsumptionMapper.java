package com.fawry.couponapi.model.mapper;

import com.fawry.couponapi.entity.Consumption;
import com.fawry.couponapi.model.dto.ConsumptionDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface ConsumptionMapper {
    @Mapping(source = "coupon.id", target = "couponId")
    @Mapping(source = "coupon.code", target = "couponCode")
    @Mapping(source = "coupon.value", target = "couponValue")
    ConsumptionDTO toDto(Consumption consumption);

    List<ConsumptionDTO> toDto(List<Consumption> consumption);
}