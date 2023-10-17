package com.fawry.couponapi.entity;

import com.fawry.couponapi.enumeration.CouponType;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "coupon")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Code is mandatory and should not be blank")
    private String code;

    @NotNull
    @Digits(integer = 10, fraction = 0, message = "Remaining usages must be integer")
    @PositiveOrZero(message = "Remaining usages should be positive or zero")
    private Integer remainingUsages;

    @NotNull(message = "Expiry date is mandatory")
    @FutureOrPresent(message = "Expiry date can't be in the past")
    private LocalDate expiryDate;

    @NotNull(message = "Value is mandatory")
    @Positive(message = "Value should be positive")
    private BigDecimal value;

    @NotNull(message = "Active is mandatory")
    private Boolean active = true;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Type is mandatory")
    private CouponType type;

}
