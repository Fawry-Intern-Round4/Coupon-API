package com.fawry.couponapi.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumptionDTO {

    private Long id;

    private Instant consumptionDate;

    private Long orderId;

    private BigDecimal actualDiscount;

    private String customerEmail;

    private Long couponId;

    private String couponCode;

    private BigDecimal couponValue;
}
