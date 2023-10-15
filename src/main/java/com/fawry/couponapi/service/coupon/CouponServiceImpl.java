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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        Coupon couponEntity = requestedCouponMapper.toEntity(couponDTO);
        couponEntity.setCode(generateCouponCode());
        couponRepository.save(couponEntity);
        return returnedCouponMapper.toDto(couponEntity);
    }

    @Override
    public ReturnedCouponDTO get(String code) {
        Optional<Coupon> coupon = couponRepository.findByCode(code);
        return returnedCouponMapper.toDto(coupon.orElseThrow(() -> new CouponException(COUPON_NOT_FOUND.getMessage())));
    }

    @Override
    public List<ReturnedCouponDTO> getAll() {
        List<Coupon> coupons = couponRepository.findAll();

        return returnedCouponMapper.toDto(coupons);
    }

    @Override
    public List<ReturnedCouponDTO> getAllActive() {
        return activeCouponFilter(true);
    }

    @Override
    public List<ReturnedCouponDTO> getAllInActive() {
        return activeCouponFilter(false);
    }

    @Override
    public void deactivate(String code) {
        Optional<Coupon> coupon = couponRepository.findByCode(code);
        if (coupon.isPresent()) {
            coupon.get().setActive(false);
            couponRepository.save(coupon.get());
        } else {
            throw new CouponException(COUPON_NOT_FOUND.getMessage());
        }
    }

    @Override
    public Boolean validateRequestCoupon(OrderRequestDTO orderRequestDTO) {
        validateCouponCode(orderRequestDTO.getCode());
        verifyCouponIsNotUsedByCustomer(orderRequestDTO);
        return true;
    }

    @Override
    public void validateCouponCode(String code) {
        Optional<Coupon> coupon = couponRepository.findByCode(code);
        if (coupon.isPresent()) {
            verifyCouponIsActive(coupon.get());
            verifyCouponCanBeUsed(coupon.get());
            verifyCouponNotOutOfDate(coupon.get());
        } else {
            throw new CouponException(COUPON_NOT_FOUND.getMessage());
        }
    }

    @Override
    public ConsumptionDTO consume(OrderRequestDTO orderRequestDTO) {
        if (Boolean.TRUE.equals(validateRequestCoupon(orderRequestDTO))) {
            applyConsumption(orderRequestDTO);
        }

        return getConsumptionDTO(orderRequestDTO);
    }

    @Override
    public DiscountDTO testConsume(OrderRequestDTO orderRequestDTO) {
        validateRequestCoupon(orderRequestDTO);

        Consumption consumption = prepareConsumption(orderRequestDTO);
        return DiscountDTO.builder()
                .actualDiscount(consumption.getActualDiscount())
                .build();
    }

    private ConsumptionDTO getConsumptionDTO(OrderRequestDTO orderRequestDTO) {
        Optional<Consumption> consumption = consumptionRepository.findByOrderIdAndCustomerEmail(
                orderRequestDTO.getOrderId(),
                orderRequestDTO.getCustomerEmail()
        );

        if (consumption.isEmpty()) {
            throw new CouponException(NO_CONSUMPTIONS_FOR_ORDER.getMessage());
        }
        return consumptionMapper.toDto(consumption.get());
    }

    @Override
    public String generateCouponCode() {
        CodeConfig config = CodeConfig.length(25)
                .withPrefix("Fawry_")
                .withCharset(CodeConfig.Charset.ALPHANUMERIC);

        return VoucherCodes.generate(config);
    }

    private List<ReturnedCouponDTO> activeCouponFilter(boolean status) {
        List<Coupon> coupons = couponRepository.findByActive(status);

        return returnedCouponMapper.toDto(coupons);
    }

    private void updateCoupon(OrderRequestDTO orderRequestDTO) {
        Optional<Coupon> coupon = couponRepository.findByCode(orderRequestDTO.getCode());
        if (coupon.isPresent()) {
            coupon.get().setRemainingUsages(coupon.get().getRemainingUsages() - 1);
            coupon.get().setActive(coupon.get().getRemainingUsages() > 0);
            couponRepository.save(coupon.get());
        }
    }

    private void applyConsumption(OrderRequestDTO orderRequestDTO) {
        saveConsumption(orderRequestDTO);
        updateCoupon(orderRequestDTO);
    }

    private void saveConsumption(OrderRequestDTO orderRequestDTO) {
        consumptionRepository.save(prepareConsumption(orderRequestDTO));
    }

    private Consumption prepareConsumption(OrderRequestDTO orderRequestDTO) {
        Coupon coupon = couponRepository.findByCode(orderRequestDTO.getCode()).get();

        return Consumption.builder()
                .customerEmail(orderRequestDTO.getCustomerEmail())
                .orderId(orderRequestDTO.getOrderId())
                .consumptionDate(LocalDateTime.now())
                .orderPrice(orderRequestDTO.getOrderPrice())
                .couponId(coupon.getId())
                .actualDiscount(calculateDiscount(
                        orderRequestDTO.getOrderPrice(),
                        coupon.getValue(),
                        coupon.getType()))
                .build();
    }

    private BigDecimal calculateDiscount(BigDecimal orderPrice, BigDecimal value, CouponType type) {
        if (type.equals(CouponType.PERCENTAGE)) {
            return orderPrice.multiply(value.divide(BigDecimal.valueOf(100)));
        } else {
            return value;
        }
    }

    private void verifyCouponIsActive(Coupon coupon) {
        if (Boolean.FALSE.equals(coupon.getActive())) {
            throw new CouponException(COUPON_NOT_ACTIVE.getMessage());
        }
    }

    private void verifyCouponCanBeUsed(Coupon coupon) {
        if (coupon.getRemainingUsages() == 0) {
            throw new CouponException(COUPON_FULLY_REDEEMED.getMessage());
        }
    }

    private void verifyCouponIsNotUsedByCustomer(OrderRequestDTO orderRequestDTO) {
        Optional<Consumption> consumption = consumptionRepository.findByOrderIdAndCustomerEmail(
                orderRequestDTO.getOrderId(),
                orderRequestDTO.getCustomerEmail()
        );

        if (consumption.isPresent()) {
            throw new CouponException(COUPON_USED_BY_CUSTOMER.getMessage());
        }
    }

    private void verifyCouponNotOutOfDate(Coupon coupon) {
        if (coupon.getExpiryDate().isBefore(LocalDate.now())) {
            throw new CouponException(COUPON_EXPIRED.getMessage());
        }
    }
}
