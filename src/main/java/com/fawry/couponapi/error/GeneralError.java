package com.fawry.couponapi.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneralError {
    private int status;
    private String message;
    private String timestamp;
    public static GeneralError generateGeneralError(int status, String message) {
        LocalDate localDate = LocalDate.now();
        return new GeneralError(status, message, localDate.toString());
    }
}
