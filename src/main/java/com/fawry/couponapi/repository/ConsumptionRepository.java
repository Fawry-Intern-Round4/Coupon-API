package com.fawry.couponapi.repository;

import com.fawry.couponapi.entity.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsumptionRepository extends JpaRepository<Consumption, Long> {
    Optional<Consumption> findByOrderId(Long orderId);

    List<Consumption> findByCustomerEmail(String customerEmail);

    Optional<Consumption> findByOrderIdAndCustomerEmail(Long orderId, String customerEmail);
}
