
package com.mmenshikov.PartyTreasurer.domain.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created: 09.08.2022 17:56
 *
 * @author MMenshikov
 */
@Data
@Accessors(chain = true)
public class Duty {
  private String from;
  private String to;
  private BigDecimal duty;
}
