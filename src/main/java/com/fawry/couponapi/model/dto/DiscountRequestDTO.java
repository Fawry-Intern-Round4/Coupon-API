package com.fawry.couponapi.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class DiscountRequestDTO {
    private String code;

    @Email
    private String customerEmail;

    @NotNull
    @Positive(message = "Value should be positive")
    private BigDecimal orderPrice;
}
