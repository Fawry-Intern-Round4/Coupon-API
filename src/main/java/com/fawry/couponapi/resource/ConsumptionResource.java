package com.fawry.couponapi.resource;

import com.fawry.couponapi.model.dto.ConsumptionDTO;
import com.fawry.couponapi.service.consumption.ConsumptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/consumption")
public class ConsumptionResource {
    private final ConsumptionService consumptionService;

    @GetMapping
    public @ResponseBody List<ConsumptionDTO> getConsumptions() {
        return consumptionService.getAll();
    }

    @GetMapping("/{customerEmail}")
    public @ResponseBody List<ConsumptionDTO> getConsumptionsByCustomerEmail(@PathVariable String customerEmail) {
        return consumptionService.getConsumptionsByCustomerEmail(customerEmail);
    }
}
