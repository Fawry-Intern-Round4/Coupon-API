package com.fawry.couponapi.resource;

import com.fawry.couponapi.model.dto.*;
import com.fawry.couponapi.model.response.CustomResponse;
import com.fawry.couponapi.service.coupon.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponResource {
    private final CouponService couponService;

    @GetMapping
    @ResponseBody
    public List<ReturnedCouponDTO> getCoupons() {
        return couponService.getAll();
    }

    @GetMapping("/{code}")
    @ResponseBody
    public ReturnedCouponDTO getCoupon(@PathVariable String code) {
        return couponService.get(code);
    }

    @PostMapping
    public ReturnedCouponDTO createCoupon(@Valid @RequestBody RequestedCouponDTO couponDTO) {
        return couponService.create(couponDTO);
    }

    @GetMapping("/active")
    @ResponseBody
    public List<ReturnedCouponDTO> getActiveCoupons() {
        return couponService.getAllActive();
    }

    @GetMapping("/inactive")
    @ResponseBody
    public List<ReturnedCouponDTO> getInactiveCoupons() {
        return couponService.getAllInActive();
    }

    @PutMapping("/deactivate/{code}")
    public ResponseEntity<CustomResponse> deactivateCoupon(@PathVariable String code) {
        couponService.deactivate(code);
        return responseHelper("Coupon deactivated successfully!!", HttpStatus.OK);
    }

    @PutMapping("/consume")
    public ConsumptionDTO consumeCoupon(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        return couponService.consume(orderRequestDTO);
    }

    @PutMapping("/test-consume")
    public DiscountDTO testConsumeCoupon(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        return couponService.testConsume(orderRequestDTO);
    }

    @PutMapping("/validate")
    public ResponseEntity<CustomResponse> validateCoupon(String code) {
        couponService.validateCouponCode(code);
        return responseHelper("Coupon validated successfully!!", HttpStatus.OK);
    }

    private ResponseEntity<CustomResponse> responseHelper(String message, HttpStatus httpStatus) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a"));

        CustomResponse errorResponse = CustomResponse.builder()
                .status(httpStatus.value())
                .message(message)
                .timestamp(timestamp)
                .build();

        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
