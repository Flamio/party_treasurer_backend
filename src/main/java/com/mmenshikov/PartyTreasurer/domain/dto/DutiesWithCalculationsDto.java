/*
 * VTB Group. Do not reproduce without permission in writing.
 * Copyright (c) 2022 VTB Group. All rights reserved.
 */

package com.mmenshikov.PartyTreasurer.domain.dto;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created: 10.08.2022 10:28
 *
 * @author MMenshikov
 */
@Data
@Accessors(chain = true)
public class DutiesWithCalculationsDto {
  private List<CalculationsByParticipant> calculations;
  private List<Duty> duties;
}
