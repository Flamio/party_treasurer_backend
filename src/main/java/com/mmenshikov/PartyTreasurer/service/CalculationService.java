package com.mmenshikov.PartyTreasurer.service;

import com.mmenshikov.PartyTreasurer.domain.dto.CalculationsByParticipant;
import com.mmenshikov.PartyTreasurer.domain.dto.Duty;
import com.mmenshikov.PartyTreasurer.domain.dto.InputDto;
import java.util.List;

/**
 * Created: 09.08.2022 16:09
 *
 * @author MMenshikov
 */
public interface CalculationService {

  List<CalculationsByParticipant> calcByParticipant(InputDto dto);

  List<Duty> calcDuties(List<CalculationsByParticipant> calculationsByParticipants);
}
