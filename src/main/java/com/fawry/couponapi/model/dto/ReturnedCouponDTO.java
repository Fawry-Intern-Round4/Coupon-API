package com.fawry.couponapi.model.dto;

import com.fawry.couponapi.enumeration.CouponType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnedCouponDTO {

    private String code;

    private Integer remainingUsages;

    private LocalDate expiryDate;

    private BigDecimal value;

    private Boolean active;

    private CouponType type;

}