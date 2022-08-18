package xyz.gigena.melicouponchallenge.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CouponDTO {
  @JsonProperty("item_ids")
  private List<String> itemIds;
  private Float amount;
}
