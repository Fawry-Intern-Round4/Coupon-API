package com.fawry.couponapi.model.mapper;

import com.fawry.couponapi.entity.Coupon;
import com.fawry.couponapi.model.dto.RequestedCouponDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface RequestedCouponMapper {
    RequestedCouponDTO toDto(Coupon coupon);

    Coupon toEntity(RequestedCouponDTO couponDto);

}