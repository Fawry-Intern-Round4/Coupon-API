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
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.Filter;
import org.hibernate.Session;
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
        return returnedCouponMapper.toDto(coupon.orElseThrow(() -> new CouponException("Coupon not found")));
    }

    @Override
    public List<ReturnedCouponDTO> getAll() {
        List<Coupon> coupons = couponRepository.findAll();
        if (coupons.isEmpty()) {
            throw new CouponException("No coupons found");
        }
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
            throw new CouponException("Coupon not found");
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
        } else {
            throw new CouponException("Coupon not found");
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
    public ConsumptionDTO testConsume(OrderRequestTestDTO orderRequestTestDTO) {
        OrderRequestDTO orderRequestDTO = helperOrderRequestDTOConverter(orderRequestTestDTO);
        validateRequestCoupon(orderRequestDTO);

        return getConsumptionDTO(orderRequestDTO);
    }

    OrderRequestDTO helperOrderRequestDTOConverter(OrderRequestTestDTO orderRequestTestDTO) {
        return OrderRequestDTO.builder()
                .code(orderRequestTestDTO.getCode())
                .customerEmail(orderRequestTestDTO.getCustomerEmail())
                .orderPrice(orderRequestTestDTO.getOrderPrice())
                .orderId(1L)
                .build();
    }

    private ConsumptionDTO getConsumptionDTO(OrderRequestDTO orderRequestDTO) {
        Optional<Consumption> consumption = consumptionRepository.findByOrderIdAndCustomerEmail(
                orderRequestDTO.getOrderId(),
                orderRequestDTO.getCustomerEmail()
        );

        if(consumption.isEmpty()) {
            throw new CouponException("Can't find the consumption for this order");
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
        if (coupons.isEmpty()) {
            throw new CouponException("No active coupons found");
        }
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
