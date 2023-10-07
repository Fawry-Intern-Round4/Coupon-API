package com.fawry.couponapi.service.coupon;

import com.fawry.couponapi.entity.Coupon;
import com.fawry.couponapi.model.dto.CouponDTO;
import com.fawry.couponapi.model.dto.OrderRequestDTO;

import java.util.List;

public interface CouponService {

    void create(CouponDTO coupon);

    CouponDTO get(String code);

    List<CouponDTO> getAll();

    List<CouponDTO> getAllActive();

    void delete(String code);

    Boolean validate(OrderRequestDTO orderRequestDTO);

    void consume(OrderRequestDTO orderRequestDTO);

    String generateCouponCode();

}
