package com.fawry.couponapi;

import com.fawry.couponapi.entity.enumeration.CouponType;
import com.fawry.couponapi.model.dto.CouponDTO;
import com.fawry.couponapi.model.mapper.CouponMapper;
import com.fawry.couponapi.service.coupon.CouponService;
import jakarta.validation.Valid;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootApplication
public class CouponApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CouponApiApplication.class, args);
    }

}
