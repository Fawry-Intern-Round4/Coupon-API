package com.fawry.couponapi.entity;

import com.fawry.couponapi.entity.enumeration.CouponType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupon", uniqueConstraints = @UniqueConstraint(columnNames = "code"))
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "remaining_usages", nullable = false)
    private Integer remainingUsages;

    @Column(name = "expiry_date", columnDefinition = "DATE", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "value", nullable = false)
    private BigDecimal value;

    @Column(name = "active")
    private Boolean active = true;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private CouponType type;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private List<Consumption> consumptions;

}
