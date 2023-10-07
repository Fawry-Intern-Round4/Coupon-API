package com.fawry.couponapi.resource;

import com.fawry.couponapi.model.dto.ConsumptionDTO;
import com.fawry.couponapi.service.consumption.ConsumptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
