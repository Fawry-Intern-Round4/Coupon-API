package com.fawry.couponapi.service.consumption;


import com.fawry.couponapi.model.dto.ConsumptionDTO;

import java.util.List;

public interface ConsumptionService {
    List<ConsumptionDTO> getAll();
}
