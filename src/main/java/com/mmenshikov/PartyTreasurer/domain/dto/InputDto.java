package com.mmenshikov.PartyTreasurer.domain.dto;

import com.mmenshikov.PartyTreasurer.validator.InputDtoConstraint;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created: 09.08.2022 16:02
 *
 * @author MMenshikov
 */
@Data
@Accessors(chain = true)
@InputDtoConstraint(message = "asf")
public class InputDto {
  @NotNull
  @NotEmpty
  private List<String> participants;

  @NotNull
  @NotEmpty
  private List<Product> products;

  @NotNull
  @NotEmpty
  private Map<String, Set<String>> purchases;

  @NotNull
  @NotEmpty
  private Map<String, Set<String>> uses;

  @Data
  @Accessors(chain = true)
  public static class Product {
    private String name;
    private BigDecimal price;
  }
}
