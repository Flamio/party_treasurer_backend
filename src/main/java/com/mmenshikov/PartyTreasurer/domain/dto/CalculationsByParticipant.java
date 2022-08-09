package com.mmenshikov.PartyTreasurer.domain.dto;

import java.math.BigDecimal;
import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created: 09.08.2022 16:07
 *
 * @author MMenshikov
 */
@Data
@Accessors(chain = true)
public class CalculationsByParticipant {
  private String name;
  private Map<String, BigDecimal> productMap;
}
