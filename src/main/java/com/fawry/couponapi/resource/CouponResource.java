package com.fawry.couponapi.resource;

import com.fawry.couponapi.model.dto.*;
import com.fawry.couponapi.service.coupon.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class CouponResource {
    private final CouponService couponService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ReturnedCouponDTO createCoupon(@Valid @RequestBody RequestedCouponDTO couponDTO) {
        return couponService.create(couponDTO);
    }

    @GetMapping
    public List<ReturnedCouponDTO> getCoupons(@RequestParam(required = false) Boolean isActive) {
        if (isActive == null) {
            return couponService.getAll();
        }
        return couponService.getAll(isActive);
    }

    @GetMapping("/{code}")
    public ReturnedCouponDTO getCoupon(@PathVariable String code) {
        return couponService.get(code);
    }

    @PutMapping("/deactivation/{code}")
    public ReturnedCouponDTO deactivateCoupon(@PathVariable String code) {
        return couponService.deactivate(code);
    }

    @PutMapping("/activation/{code}")
    public ReturnedCouponDTO activateCoupon(@PathVariable String code) {
        return couponService.activate(code);
    }

    @GetMapping("/validation")
    @ResponseStatus(code = HttpStatus.OK)
    public void validateCoupon(@RequestParam String code, @RequestParam String customerEmail) {
        ValidationRequestDto validationRequestDto = ValidationRequestDto.builder()
                .code(code)
                .customerEmail(customerEmail)
                .build();
        couponService.checkCouponCode(validationRequestDto);
    }

    @GetMapping("/discount")
    public DiscountDTO calculateDiscount(@RequestParam String code, @RequestParam String customerEmail,
                                         @RequestParam BigDecimal orderPrice) {
        DiscountRequestDTO discountRequestDTO = DiscountRequestDTO.builder()
                .code(code)
                .customerEmail(customerEmail)
                .orderPrice(orderPrice)
                .build();
        return couponService.calculateDiscount(discountRequestDTO);
    }

    @PostMapping("/consumption")
    public ConsumptionDTO consumeCoupon(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        return couponService.consume(orderRequestDTO);
    }
}
