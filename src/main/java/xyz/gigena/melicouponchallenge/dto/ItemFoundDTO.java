package xyz.gigena.melicouponchallenge.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItemFoundDTO extends ItemDTO {
  private final Double price;

  public ItemFoundDTO(String name, Double price) {
    super(name);
    this.price = price;
  }
}
