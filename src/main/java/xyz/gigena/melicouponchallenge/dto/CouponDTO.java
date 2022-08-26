package xyz.gigena.melicouponchallenge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponDTO {
  @JsonProperty("item_ids")
  private List<String> itemIds;

  private Double amount;

  public CouponDTO(Set<ItemDTO> items, Double maxAmount) {
    this.amount = maxAmount;
    this.itemIds = new ArrayList<String>();
    for (ItemDTO item : items) {
      this.itemIds.add(item.getName());
    }
  }
}
