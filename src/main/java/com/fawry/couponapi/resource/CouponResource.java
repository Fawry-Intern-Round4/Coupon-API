package com.fawry.couponapi.resource;

import com.fawry.couponapi.model.dto.RequestedCouponDTO;
import com.fawry.couponapi.model.dto.ReturnedCouponDTO;
import com.fawry.couponapi.model.dto.OrderRequestDTO;
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

    @GetMapping("/deleted")
    @ResponseBody
    public List<ReturnedCouponDTO> getDeletedCoupons() {
        return couponService.getAll(Boolean.TRUE);
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

    @DeleteMapping("/{code}")
    public ResponseEntity<CustomResponse> deleteCoupon(@PathVariable String code) {
        couponService.delete(code);
        return responseHelper("Coupon deleted successfully!!", HttpStatus.OK);
    }

    @GetMapping("/active")
    @ResponseBody
    public List<ReturnedCouponDTO> getActiveCoupons() {
        return couponService.getAllActive();
    }

    @PutMapping("/deactivate/{code}")
    public ResponseEntity<CustomResponse> deactivateCoupon(@PathVariable String code) {
        couponService.deactivate(code);
        return responseHelper("Coupon deactivated successfully!!", HttpStatus.OK);
    }

    @PutMapping("/consume")
    public ResponseEntity<CustomResponse> consumeCoupon(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        couponService.consume(orderRequestDTO);
        return responseHelper("Coupon consumed successfully!!", HttpStatus.OK);
    }

    @GetMapping("/generate")
    public String getCouponCode() {
        return couponService.generateCouponCode();
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
