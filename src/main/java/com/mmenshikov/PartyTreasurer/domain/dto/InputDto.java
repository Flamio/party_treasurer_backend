package com.mmenshikov.PartyTreasurer.domain.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created: 09.08.2022 16:02
 *
 * @author MMenshikov
 */
@Data
@Accessors(chain = true)
public class InputDto {
  private List<String> participants;
  private List<Product> products;

  private Map<String, Set<String>> purchases;
  private Map<String, Set<String>> uses;

  @Data
  @Accessors(chain = true)
  public static class Product {
    private String name;
    private BigDecimal price;
  }
}
