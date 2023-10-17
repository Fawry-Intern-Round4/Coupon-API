package com.fawry.couponapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "consumption")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Consumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Instant consumptionDate;

    @NotNull(message = "Order id is mandatory")
    private Long orderId;

    @NotNull(message = "Customer email is mandatory")
    @Email(message = "Customer email should be valid")
    private String customerEmail;

    @NotNull(message = "Actual discount is mandatory")
    @Positive(message = "Actual discount should be positive")
    private BigDecimal actualDiscount;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
}
