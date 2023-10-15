package com.fawry.couponapi.model.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated

public class DiscountDTO {

    @NotNull
    @PositiveOrZero
    private BigDecimal actualDiscount;

}

