package com.fawry.couponapi.resource;

import com.fawry.couponapi.model.dto.CouponDTO;
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
    public List<CouponDTO> getCoupons() {
        return couponService.getAll();
    }

    @GetMapping("/{code}")
    @ResponseBody
    public CouponDTO getCoupon(@PathVariable String code) {
        return couponService.get(code);
    }

    @PostMapping
    public ResponseEntity<CustomResponse> createCoupon(@Valid @RequestBody CouponDTO couponDTO) {
        couponService.create(couponDTO);
        return responseHelper("Coupon created successfully!!", HttpStatus.CREATED);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<CustomResponse> deleteCoupon(@PathVariable String code) {
        couponService.delete(code);
        return responseHelper("Coupon deleted successfully!!", HttpStatus.OK);
    }

    @GetMapping("/active")
    @ResponseBody
    public List<CouponDTO> getActiveCoupons() {
        return couponService.getAllActive();
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
    public ResponseEntity<CustomResponse> validateCoupon(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        Boolean isValid = couponService.validate(orderRequestDTO);

        return Boolean.TRUE.equals(isValid) ?
                responseHelper("Coupon is valid!!", HttpStatus.OK) :
                responseHelper("Coupon is not valid!!", HttpStatus.BAD_REQUEST);
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
