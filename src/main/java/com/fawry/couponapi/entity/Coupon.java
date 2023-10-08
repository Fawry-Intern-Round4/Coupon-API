package com.fawry.couponapi.entity;

import com.fawry.couponapi.entity.enumeration.CouponType;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE coupon SET deleted = true WHERE id=?")
@FilterDef(name = "deletedProductFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedProductFilter", condition = "deleted = :isDeleted")
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

    @Column(name = "deleted")
    private Boolean deleted = Boolean.FALSE;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private List<Consumption> consumptions;

}
