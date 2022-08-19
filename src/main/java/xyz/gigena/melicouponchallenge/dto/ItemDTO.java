package xyz.gigena.melicouponchallenge.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ItemDTO {
  protected String name;

  public ItemDTO(String name) {
    this.name = name;
  }

  public abstract Double getPrice();
}
