
package com.mmenshikov.PartyTreasurer;

import com.mmenshikov.PartyTreasurer.domain.dto.InputDto;
import com.mmenshikov.PartyTreasurer.domain.dto.InputDto.Product;
import com.mmenshikov.PartyTreasurer.service.CalculationService;
import com.mmenshikov.PartyTreasurer.service.impl.CalculationServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created: 09.08.2022 17:13
 *
 * @author MMenshikov
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
class ParticipantsServiceTest {

  @Autowired
  private CalculationService service;

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

    final var romaDuties = duties.stream().filter(duty -> duty.getFrom().equals("roma"))
        .collect(Collectors.toList());

    final var andrDuty = duties.stream().filter(duty -> duty.getFrom().equals("andr"))
        .findFirst();

    Assertions.assertThat(romaDuties.get(0).getDuty().intValue())
        .isEqualTo(75);
    Assertions.assertThat(romaDuties.get(0).getTo())
        .isEqualTo("petya");

    Assertions.assertThat(romaDuties.get(1).getDuty().intValue())
        .isEqualTo(450);
    Assertions.assertThat(romaDuties.get(1).getTo())
        .isEqualTo("vasya");

    Assertions.assertThat(andrDuty.get().getDuty().intValue())
        .isEqualTo(25);
    Assertions.assertThat(andrDuty.get().getTo())
        .isEqualTo("vasya");

  }

  @Test
  void unusedProductsTest() {
    //arrange

    var inputDto = new InputDto()
        .setParticipants(List.of("petya", "vasya", "roma", "andr"))
        .setProducts(List.of(new Product()
                .setName("pivo")
                .setPrice(BigDecimal.valueOf(100)),
            new Product()
                .setName("vino")
                .setPrice(BigDecimal.valueOf(1000)),
            new Product()
                .setName("meat")
                .setPrice(BigDecimal.valueOf(3000))))
        .setPurchases(
            Map.of("petya", Set.of("pivo"), "vasya", Set.of("vino"), "roma", Set.of("meat")))
        .setUses(Map.of("petya", Set.of("pivo"), "vasya", Set.of("vino", "pivo"), "roma",
            Set.of("vino", "pivo"), "andr", Set.of("pivo")));

    //act
    final var result = service.calcAll(inputDto);

    final var duties = result.getDuties();

    final var romaDuties = duties.stream().filter(duty -> duty.getFrom().equals("roma"))
        .collect(Collectors.toList());

    final var andrDuty = duties.stream().filter(duty -> duty.getFrom().equals("andr"))
        .findFirst();

    //assert
    var meatExists = result.getCalculations()
        .stream()
        .flatMap(calculationsByParticipant ->
            calculationsByParticipant
                .getProductMap()
                .keySet()
                .stream())
        .anyMatch(productName -> productName.equals("meat"));

    Assertions.assertThat(meatExists)
        .isFalse();

    Assertions.assertThat(result.getUnusableProducts().size())
        .isEqualTo(1);
    Assertions.assertThat(result.getUnusableProducts().get(0))
        .isEqualTo("meat");

    Assertions.assertThat(romaDuties.get(0).getDuty().intValue())
        .isEqualTo(75);
    Assertions.assertThat(romaDuties.get(0).getTo())
        .isEqualTo("petya");

    Assertions.assertThat(romaDuties.get(1).getDuty().intValue())
        .isEqualTo(450);
    Assertions.assertThat(romaDuties.get(1).getTo())
        .isEqualTo("vasya");

    Assertions.assertThat(andrDuty.get().getDuty().intValue())
        .isEqualTo(25);
    Assertions.assertThat(andrDuty.get().getTo())
        .isEqualTo("vasya");
  }
}
