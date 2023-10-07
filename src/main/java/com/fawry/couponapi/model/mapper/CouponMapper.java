package com.fawry.couponapi.model.mapper;

import com.fawry.couponapi.entity.Coupon;
import com.fawry.couponapi.model.dto.CouponDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface CouponMapper {
    CouponDTO toDto(Coupon coupon);

    List<CouponDTO> toDto(List<Coupon> coupons);

    Coupon toEntity(CouponDTO couponDto);

    List<Coupon> toEntity(List<CouponDTO> couponDto);
}