package xyz.gigena.melicouponchallenge.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import xyz.gigena.melicouponchallenge.dto.ItemDTO;

@Entity
@Table(name = "coupon")
@Getter
@Setter
public class Coupon {

  @Id
  @SequenceGenerator(name = "coupon_sequence", sequenceName = "coupon_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coupon_sequence")
  private long couponId;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(
      name = "coupon_item",
      joinColumns = @JoinColumn(name = "coupon_id", referencedColumnName = "couponId"),
      inverseJoinColumns = @JoinColumn(name = "item_id", referencedColumnName = "itemId"))
  private Set<Item> itemIds;

  @Column(name = "amount_cents")
  private Long amount;

  @Column(name = "spent_amount_cents")
  private Long spentAmount;

  public Coupon(Set<ItemDTO> items, Long amountLimit, Long spentAmount) {
    this.itemIds = new HashSet<Item>();
    for (ItemDTO item : items) {
      Item i = new Item(item.getName());
      this.itemIds.add(i);
    }
    this.amount = amountLimit;
    this.spentAmount = spentAmount;
  }
}
