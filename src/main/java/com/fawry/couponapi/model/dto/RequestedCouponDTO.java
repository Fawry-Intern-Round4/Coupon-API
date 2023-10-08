package com.fawry.couponapi.model.dto;

import com.fawry.couponapi.entity.enumeration.CouponType;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class RequestedCouponDTO {

    @NotNull
    @PositiveOrZero(message = "Remaining usages must be positive")
    private Integer remainingUsages;

    @FutureOrPresent(message = "Expiry date can't be in the past")
    private LocalDate expiryDate;

    @NotNull
    @Positive(message = "Value should be positive")
    private BigDecimal value;

    @NotNull
    private Boolean active;

    @NotNull
    private CouponType type;

}