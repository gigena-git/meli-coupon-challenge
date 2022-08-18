package xyz.gigena.melicouponchallenge.dto;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItemSetDTO {
  private final int total;
  private int notFound;
  private Set<ItemDTO> items;

  public ItemSetDTO(int total) {
    this.total = total;
    this.notFound = 0;
  }
}
