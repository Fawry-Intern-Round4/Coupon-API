package com.fawry.couponapi.model.mapper;

import com.fawry.couponapi.entity.Coupon;
import com.fawry.couponapi.model.dto.ReturnedCouponDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface InternalCouponMapper {
    ReturnedCouponDTO toDto(Coupon coupon);

    List<ReturnedCouponDTO> toDto(List<Coupon> coupons);

    Coupon toEntity(ReturnedCouponDTO couponDto);

    List<Coupon> toEntity(List<ReturnedCouponDTO> couponDto);
}