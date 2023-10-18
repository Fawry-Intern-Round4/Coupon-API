package com.fawry.couponapi.service.coupon;

import com.fawry.couponapi.entity.Consumption;
import com.fawry.couponapi.entity.Coupon;
import com.fawry.couponapi.enumeration.CouponType;
import com.fawry.couponapi.exception.CouponException;
import com.fawry.couponapi.generator.CodeConfig;
import com.fawry.couponapi.generator.VoucherCodes;
import com.fawry.couponapi.model.dto.*;
import com.fawry.couponapi.model.mapper.ConsumptionMapper;
import com.fawry.couponapi.model.mapper.RequestedCouponMapper;
import com.fawry.couponapi.model.mapper.ReturnedCouponMapper;
import com.fawry.couponapi.repository.ConsumptionRepository;
import com.fawry.couponapi.repository.CouponRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.fawry.couponapi.enumeration.ExceptionMessages.*;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final ConsumptionRepository consumptionRepository;
    private final RequestedCouponMapper requestedCouponMapper;
    private final ReturnedCouponMapper returnedCouponMapper;
    private final ConsumptionMapper consumptionMapper;

    @Override
    public ReturnedCouponDTO create(@Valid RequestedCouponDTO couponDTO) {
        if (couponDTO.getType().equals(CouponType.PERCENTAGE)
                && couponDTO.getValue().compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new CouponException(PERCENTAGE_VALUE_EXCEEDS_100.getMessage(), HttpStatus.BAD_REQUEST);
        }
        Coupon couponEntity = requestedCouponMapper.toEntity(couponDTO);
        couponEntity.setCode(generateCouponCode());
        couponRepository.save(couponEntity);
        return returnedCouponMapper.toDto(couponEntity);
    }

    @Override
    public ReturnedCouponDTO get(String code) {
        Coupon coupon = findCouponByCode(code);
        return returnedCouponMapper.toDto(coupon);
    }

    @Override
    public List<ReturnedCouponDTO> getAll() {
        List<Coupon> coupons = couponRepository.findAll();
        return returnedCouponMapper.toDto(coupons);
    }

    @Override
    public List<ReturnedCouponDTO> getAll(Boolean isActive) {
        List<Coupon> coupons = couponRepository.findByActive(isActive);
        return returnedCouponMapper.toDto(coupons);
    }

    @Override
    public ReturnedCouponDTO deactivate(String code) {
        Coupon coupon = findCouponByCode(code);
        coupon.setActive(false);
        return returnedCouponMapper.toDto(couponRepository.save(coupon));
    }

    @Override
    public ReturnedCouponDTO activate(String code) {
        Coupon coupon = findCouponByCode(code);
        coupon.setActive(true);
        return returnedCouponMapper.toDto(couponRepository.save(coupon));
    }

    @Override
    public void checkCouponCode(ValidationRequestDto validationRequestDto) {
        Coupon coupon = findCouponByCode(validationRequestDto.getCode());
        validateCouponCode(validationRequestDto.getCode());
        checkIfCouponIsUsedByCustomer(coupon, validationRequestDto.getCustomerEmail());
    }

    public DiscountDTO calculateDiscount(@Valid DiscountRequestDTO discountRequestDTO) {
        validateCouponCode(discountRequestDTO.getCode());
        Coupon coupon = findCouponByCode(discountRequestDTO.getCode());
        return DiscountDTO.builder()
                .actualDiscount(calculateDiscount(
                        discountRequestDTO.getOrderPrice(),
                        coupon.getValue(),
                        coupon.getType()))
                .build();
    }


    @Override
    public ConsumptionDTO consume(OrderRequestDTO orderRequestDTO) {
        validateCouponCode(orderRequestDTO.getCode());
        Coupon coupon = findCouponByCode(orderRequestDTO.getCode());
        checkIfCouponIsUsedByCustomer(coupon, orderRequestDTO.getCustomerEmail());
        useCouponInDatabase(coupon);
        return consumptionMapper.toDto(createConsumptionInDatabase(coupon, orderRequestDTO));
    }


    @Override
    public String generateCouponCode() {
        CodeConfig config = CodeConfig.length(25)
                .withPrefix("Fawry_")
                .withCharset(CodeConfig.Charset.ALPHANUMERIC);

        return VoucherCodes.generate(config);
    }

    private Coupon findCouponByCode(String code) {
        return couponRepository.findByCode(code)
                .orElseThrow(() -> new CouponException(COUPON_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND));
    }

    private BigDecimal calculateDiscount(BigDecimal orderPrice, BigDecimal value, CouponType type) {
        if (type.equals(CouponType.PERCENTAGE)) {
            return orderPrice.multiply(value.divide(BigDecimal.valueOf(100)));
        } else {
            return orderPrice.min(value);
        }
    }

    private void validateCouponCode(String code) {
        Coupon coupon = findCouponByCode(code);
        if (Boolean.FALSE.equals(coupon.getActive())) {
            throw new CouponException(COUPON_NOT_ACTIVE.getMessage(), HttpStatus.BAD_REQUEST);
        }
        if (coupon.getRemainingUsages() == 0) {
            throw new CouponException(COUPON_FULLY_REDEEMED.getMessage(), HttpStatus.BAD_REQUEST);
        }
        if (coupon.getExpiryDate().isBefore(LocalDate.now())) {
            throw new CouponException(COUPON_EXPIRED.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private void checkIfCouponIsUsedByCustomer(Coupon coupon, String customerEmail) {
        consumptionRepository.findByCoupon_IdAndCustomerEmail(coupon.getId(), customerEmail)
                .ifPresent(consumption -> {
                    throw new CouponException(COUPON_USED_BY_CUSTOMER.getMessage(), HttpStatus.BAD_REQUEST);
                });
    }

    private void useCouponInDatabase(Coupon coupon) {
        coupon.setRemainingUsages(coupon.getRemainingUsages() - 1);
        coupon.setActive(coupon.getRemainingUsages() > 0);
        couponRepository.save(coupon);
    }

    private Consumption createConsumptionInDatabase(Coupon coupon, OrderRequestDTO orderRequestDTO) {
        Consumption consumption = Consumption.builder()
                .customerEmail(orderRequestDTO.getCustomerEmail())
                .orderId(orderRequestDTO.getOrderId())
                .coupon(coupon)
                .actualDiscount(calculateDiscount(
                        orderRequestDTO.getOrderPrice(),
                        coupon.getValue(),
                        coupon.getType()))
                .build();
        return consumptionRepository.save(consumption);
    }

}
