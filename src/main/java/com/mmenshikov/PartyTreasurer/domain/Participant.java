package com.mmenshikov.PartyTreasurer.domain;

import java.math.BigDecimal;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created: 09.08.2022 18:09
 *
 * @author MMenshikov
 */
@Data
@Accessors(chain = true)
public class Participant {

  private String name;
  private BigDecimal total;
}
