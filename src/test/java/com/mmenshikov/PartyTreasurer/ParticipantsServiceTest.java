
package com.mmenshikov.PartyTreasurer;

import com.mmenshikov.PartyTreasurer.domain.dto.InputDto;
import com.mmenshikov.PartyTreasurer.domain.dto.InputDto.Product;
import com.mmenshikov.PartyTreasurer.service.CalculationService;
import com.mmenshikov.PartyTreasurer.service.impl.CalculationServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created: 09.08.2022 17:13
 *
 * @author MMenshikov
 */
class ParticipantsServiceTest {

  private CalculationService service = new CalculationServiceImpl();

  @Test
  void calculationByParticipantTest() {
    var inputDto = new InputDto()
        .setParticipants(List.of("petya", "vasya", "roma"))
        .setProducts(List.of(new Product()
                .setName("pivo")
                .setPrice(BigDecimal.valueOf(99)),
            new Product()
                .setName("vino")
                .setPrice(BigDecimal.valueOf(1000))))
        .setPurchases(Map.of("petya", Set.of("pivo"), "vasya", Set.of("vino")))
        .setUses(Map.of("petya", Set.of("pivo"), "vasya", Set.of("vino", "pivo"), "roma",
            Set.of("vino", "pivo")));

    var participants = service.calcByParticipant(inputDto);

    Assertions.assertThat(participants.size())
        .isEqualTo(3);

    Assertions.assertThat(participants.get(0).getProductMap().size())
        .isEqualTo(1);
    Assertions.assertThat(participants.get(0).getProductMap().get("pivo").toString())
        .isEqualTo("66.00");

    Assertions.assertThat(participants.get(1).getProductMap().size())
        .isEqualTo(2);
    Assertions.assertThat(participants.get(1).getProductMap().get("pivo").toString())
        .isEqualTo("-33.00");
    Assertions.assertThat(participants.get(1).getProductMap().get("vino").toString())
        .isEqualTo("500.00");

    Assertions.assertThat(participants.get(2).getProductMap().size())
        .isEqualTo(2);

    Assertions.assertThat(participants.get(2).getProductMap().get("pivo").toString())
        .isEqualTo("-33.00");
    Assertions.assertThat(participants.get(2).getProductMap().get("vino").toString())
        .isEqualTo("-500.00");
  }

  @Test
  void calcDutiesTest() {

    var inputDto = new InputDto()
        .setParticipants(List.of("petya", "vasya", "roma", "andr"))
        .setProducts(List.of(new Product()
                .setName("pivo")
                .setPrice(BigDecimal.valueOf(100)),
            new Product()
                .setName("vino")
                .setPrice(BigDecimal.valueOf(1000))))
        .setPurchases(Map.of("petya", Set.of("pivo"), "vasya", Set.of("vino")))
        .setUses(Map.of("petya", Set.of("pivo"), "vasya", Set.of("vino", "pivo"), "roma",
            Set.of("vino", "pivo"), "andr", Set.of("pivo")));

    var participants = service.calcByParticipant(inputDto);
    var duties = service.calcDuties(participants);

    Assertions.assertThat(duties)
        .isNotNull();
  }
}
