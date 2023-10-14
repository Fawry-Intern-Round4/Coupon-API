package com.fawry.couponapi.service.coupon;

import com.fawry.couponapi.model.dto.*;

import java.util.List;

public interface CouponService {

    ReturnedCouponDTO create(RequestedCouponDTO coupon);

    ReturnedCouponDTO get(String code);

    List<ReturnedCouponDTO> getAll();

    List<ReturnedCouponDTO> getAllActive();

    List<ReturnedCouponDTO> getAllInActive();

    void deactivate(String code);

    Boolean validateRequestCoupon(OrderRequestDTO orderRequestDTO);

    void validateCouponCode(String code);

    ConsumptionDTO consume(OrderRequestDTO orderRequestDTO);

    String generateCouponCode();

    ConsumptionDTO testConsume(OrderRequestTestDTO orderRequestDTO);

}
