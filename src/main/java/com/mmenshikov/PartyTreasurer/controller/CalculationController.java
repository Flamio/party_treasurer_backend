package com.mmenshikov.PartyTreasurer.controller;

import com.mmenshikov.PartyTreasurer.domain.dto.DutiesWithCalculationsDto;
import com.mmenshikov.PartyTreasurer.domain.dto.InputDto;
import com.mmenshikov.PartyTreasurer.service.CalculationService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created: 10.08.2022 10:26
 *
 * @author MMenshikov
 */

@RequestMapping("/api/v1/calculations")
@RestController
@RequiredArgsConstructor
public class CalculationController {

  private final CalculationService calculationService;

  @PostMapping("/process")
  DutiesWithCalculationsDto process(@RequestBody @Valid final InputDto inputDto) {
    return calculationService.calcAll(inputDto);
  }
}
