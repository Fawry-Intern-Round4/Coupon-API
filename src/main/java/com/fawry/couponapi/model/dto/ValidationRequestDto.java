package com.fawry.couponapi.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class ValidationRequestDto {
    @NotBlank(message = "Code is mandatory and should not be blank")
    private String code;

    @Email
    private String customerEmail;
}
