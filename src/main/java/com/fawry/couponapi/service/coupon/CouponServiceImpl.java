package com.fawry.couponapi.service.coupon;

import com.fawry.couponapi.entity.Consumption;
import com.fawry.couponapi.entity.Coupon;
import com.fawry.couponapi.entity.enumeration.CouponType;
import com.fawry.couponapi.exception.CouponException;
import com.fawry.couponapi.generator.CodeConfig;
import com.fawry.couponapi.generator.VoucherCodes;
import com.fawry.couponapi.model.dto.CouponDTO;
import com.fawry.couponapi.model.dto.OrderRequestDTO;
import com.fawry.couponapi.model.mapper.CouponMapper;
import com.fawry.couponapi.repository.ConsumptionRepository;
import com.fawry.couponapi.repository.CouponRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final ConsumptionRepository consumptionRepository;
    private final CouponMapper couponMapper;

    @Override
    public void create(@Valid CouponDTO couponDTO) {
        Coupon couponEntity = couponMapper.toEntity(couponDTO);
        couponRepository.save(couponEntity);
    }

    @Override
    public CouponDTO get(String code) {
        Optional<Coupon> coupon = couponRepository.findByCode(code);
        return couponMapper.toDto(coupon.orElseThrow(() -> new CouponException("Coupon not found")));
    }

    @Override
    public List<CouponDTO> getAll() {
        List<Coupon> coupons = couponRepository.findAll();
        if (coupons.isEmpty()) {
            throw new CouponException("No coupons found");
        }
        return couponMapper.toDto(coupons);
    }

    @Override
    public List<CouponDTO> getAllActive() {
        List<Coupon> coupons = couponRepository.findByActive(true);
        if (coupons.isEmpty()) {
            throw new CouponException("No active coupons found");
        }
        return couponMapper.toDto(coupons);
    }

    @Override
    public void delete(String code) {
        couponRepository.deleteByCode(code);
    }

    @Override
    public Boolean validate(OrderRequestDTO orderRequestDTO) {
        Optional<Coupon> coupon = couponRepository.findByCode(orderRequestDTO.getCode());
        if (coupon.isPresent()) {
            verifyCouponIsActive(coupon.get());
            verifyCouponCanBeUsed(coupon.get());
            verifyCouponIsNotUsedByCustomer(orderRequestDTO);
        } else {
            throw new CouponException("Coupon not found");
        }
        return true;
    }

    @Override
    public void consume(OrderRequestDTO orderRequestDTO) {
        if (Boolean.TRUE.equals(validate(orderRequestDTO))) {
            applyConsumption(orderRequestDTO);
        }
    }

    @Override
    public String generateCouponCode() {
        CodeConfig config = CodeConfig.length(25)
                .withPrefix("Fawry_")
                .withCharset(CodeConfig.Charset.ALPHANUMERIC);

        return VoucherCodes.generate(config);
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
        Coupon coupon = couponRepository.findByCode(orderRequestDTO.getCode()).get();

        Consumption consumption = Consumption.builder()
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

        consumptionRepository.save(consumption);
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
            throw new CouponException("Coupon is not active currently");
        }
    }

    private void verifyCouponCanBeUsed(Coupon coupon) {
        if (coupon.getRemainingUsages() == 0) {
            throw new CouponException("Coupon is fully redeemed");
        }
    }

    private void verifyCouponIsNotUsedByCustomer(OrderRequestDTO orderRequestDTO) {
        Optional<Consumption> consumption = consumptionRepository.findByOrderIdAndCustomerEmail(
                orderRequestDTO.getOrderId(),
                orderRequestDTO.getCustomerEmail()
        );

        if (consumption.isPresent()) {
            throw new CouponException("Coupon already used by this customer");
        }
    }

}
