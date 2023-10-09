package com.fawry.couponapi.service.coupon;

import com.fawry.couponapi.model.dto.RequestedCouponDTO;
import com.fawry.couponapi.model.dto.ReturnedCouponDTO;
import com.fawry.couponapi.model.dto.OrderRequestDTO;

import java.util.List;

public interface CouponService {

    ReturnedCouponDTO create(RequestedCouponDTO coupon);

    ReturnedCouponDTO get(String code);

    List<ReturnedCouponDTO> getAll();

    List<ReturnedCouponDTO> getAllActive();

    void delete(String code);

    void deactivate(String code);

    Boolean validateRequestCoupon(OrderRequestDTO orderRequestDTO);

    void validateCouponCode(String code);

    void consume(OrderRequestDTO orderRequestDTO);

    String generateCouponCode();

    List<ReturnedCouponDTO> getAll(Boolean isDeleted);
}
