package com.mmenshikov.PartyTreasurer.service.impl;

import com.mmenshikov.PartyTreasurer.domain.Participant;
import com.mmenshikov.PartyTreasurer.domain.dto.CalculationsByParticipant;
import com.mmenshikov.PartyTreasurer.domain.dto.Duty;
import com.mmenshikov.PartyTreasurer.domain.dto.InputDto;
import com.mmenshikov.PartyTreasurer.domain.dto.InputDto.Product;
import com.mmenshikov.PartyTreasurer.service.CalculationService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Created: 09.08.2022 16:16
 *
 * @author MMenshikov
 */
@Service
public class CalculationServiceImpl implements CalculationService {

  @Override
  public List<CalculationsByParticipant> calcByParticipant(InputDto dto) {
    return dto.getParticipants()
        .stream()
        .map(p -> mapToCalculations(p, dto))
        .collect(Collectors.toList());
  }

  @Override
  public List<Duty> calcDuties(List<CalculationsByParticipant> calculationsByParticipants) {
    var participantsWithDuties = calculationsByParticipants
        .stream()
        .map(calculationsByParticipant -> new Participant()
            .setName(calculationsByParticipant.getName())
            .setTotal(
                calculationsByParticipant
                    .getProductMap()
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .get()))
        .collect(Collectors.toList());

    var result = new ArrayList<Duty>();

    while (participantsWithDuties.stream()
        .anyMatch(this::isDebtor)) {

      participantsWithDuties.stream()
          .filter(this::isDebtor)
          .forEach(participant -> {
            var notDebtor = participantsWithDuties.stream()
                .filter(this::isNotDebtor)
                .findFirst().get();
            if (participant.getTotal().abs().compareTo(notDebtor.getTotal()) <= 0) {
              result.add(new Duty()
                  .setFrom(participant.getName())
                  .setTo(notDebtor.getName())
                  .setDuty(participant.getTotal().abs()));
              notDebtor.setTotal(notDebtor.getTotal().add(participant.getTotal()));
              participant.setTotal(BigDecimal.ZERO);
              return;
            }
            result.add(new Duty()
                .setFrom(participant.getName())
                .setTo(notDebtor.getName())
                .setDuty(notDebtor.getTotal()));
            participant.setTotal(participant.getTotal().add(notDebtor.getTotal()));
            notDebtor.setTotal(BigDecimal.ZERO);
          });
    }

    return result;
  }

  private boolean isDebtor(Participant participant) {
    return participant.getTotal().compareTo(BigDecimal.ZERO) < 0;
  }

  private boolean isNotDebtor(Participant participant) {
    return participant.getTotal().compareTo(BigDecimal.ZERO) > 0;
  }

  private CalculationsByParticipant mapToCalculations(final String participantName,
      final InputDto dto) {
    var purchases = dto.getPurchases().get(participantName);
    var productMap = purchases == null ? new HashMap<String, BigDecimal>() : purchases
        .stream()
        .collect(Collectors.toMap(s -> s,
            v -> dto.getProducts().stream().filter(product -> product.getName().equals(v))
                .findFirst().get().getPrice()));
    var uses = dto.getUses().get(participantName);

    uses.forEach((p) -> updatePriceInParticipantProductMap(p, productMap, dto));

    return new CalculationsByParticipant().setName(participantName).setProductMap(productMap);
  }

  private void updatePriceInParticipantProductMap(final String usingProductName,
      final Map<String, BigDecimal> productMap, final InputDto dto) {
    var participantProductPrice = productMap.get(usingProductName);
    var allUsesSize = dto.getUses().values()
        .stream().flatMap(Collection::stream)
        .filter(s -> s.equals(usingProductName)).count();

    productMap.put(usingProductName,
        calcNewPrice(participantProductPrice, allUsesSize, dto.getProducts(),
            usingProductName).setScale(2, RoundingMode.HALF_UP));
  }

  private BigDecimal calcNewPrice(final BigDecimal participantProductPrice, long allUsesSize,
      final List<Product> products, final String usingProductName) {
    if (participantProductPrice != null) {
      return participantProductPrice.subtract(
          participantProductPrice.setScale(2, RoundingMode.HALF_UP).divide(
              BigDecimal.valueOf(allUsesSize), RoundingMode.HALF_UP));
    }

    var productPrice = products
        .stream()
        .filter(p -> p.getName().equals(usingProductName))
        .findFirst().get().getPrice();
    return BigDecimal.ZERO.subtract(productPrice.setScale(2, RoundingMode.HALF_UP).divide(
        BigDecimal.valueOf(allUsesSize), RoundingMode.HALF_UP));
  }
}
