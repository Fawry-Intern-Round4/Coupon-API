package com.fawry.couponapi.model.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated

public class ConsumptionDTO {

    @NotNull
    private Long id;

    @NotNull
    private LocalDateTime consumptionDate;

    @NotNull
    private Long orderId;

    @Positive(message = "Value should be positive")
    private BigDecimal orderPrice;

    @NotNull
    @PositiveOrZero
    private BigDecimal actualDiscount;

    @Email
    @NotNull
    private String customerEmail;
}
