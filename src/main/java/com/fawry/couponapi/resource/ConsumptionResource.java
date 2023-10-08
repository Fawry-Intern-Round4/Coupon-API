package com.fawry.couponapi.resource;

import com.fawry.couponapi.model.dto.ConsumptionDTO;
import com.fawry.couponapi.service.consumption.ConsumptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/consumptions")
public class ConsumptionResource {
    private final ConsumptionService consumptionService;

    @GetMapping
    public List<ConsumptionDTO> getConsumptions() {
        return consumptionService.getAll();
    }

    @GetMapping("/customer/{customerEmail}")
    @ResponseBody
    public List<ConsumptionDTO> getConsumptionsByCustomerEmail(@PathVariable String customerEmail) {
        return consumptionService.getConsumptionsByCustomerEmail(customerEmail);
    }
}
