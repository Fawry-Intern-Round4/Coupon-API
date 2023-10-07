package com.fawry.couponapi.service.consumption;

import com.fawry.couponapi.model.dto.ConsumptionDTO;
import com.fawry.couponapi.model.mapper.ConsumptionMapper;
import com.fawry.couponapi.repository.ConsumptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsumptionServiceImpl implements ConsumptionService {

    private final ConsumptionRepository consumptionRepository;
    private final ConsumptionMapper consumptionMapper;

    @Override
    public List<ConsumptionDTO> getAll() {
        return consumptionMapper.toDto(consumptionRepository.findAll());
    }
}
