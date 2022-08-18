package xyz.gigena.melicouponchallenge.entity;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "coupon")
@Getter
@Setter
@NoArgsConstructor
public class Coupon {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  /* @OneToMany(mappedBy = "item_ids")
  private List<String> itemIds; */

  private Float amount;
}
