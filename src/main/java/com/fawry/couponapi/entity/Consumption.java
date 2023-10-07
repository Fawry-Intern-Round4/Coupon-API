package com.fawry.couponapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "consumption")
public class Consumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "consumption_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime consumptionDate;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "customer_email", nullable = false)
    private String customerEmail;

    @Column(name = "order_price", nullable = false)
    private BigDecimal orderPrice;

    @Column(name = "actual_discount", nullable = false)
    private BigDecimal actualDiscount;

    @Column(name = "coupon_id", nullable = false)
    private Long couponId;
}
