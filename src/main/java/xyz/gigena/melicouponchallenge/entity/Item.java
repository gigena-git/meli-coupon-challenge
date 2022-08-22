package xyz.gigena.melicouponchallenge.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
    name = "item",
    uniqueConstraints = @UniqueConstraint(name = "item_name_unique", columnNames = "name"))
@Setter
@Getter
public class Item {
  @Id
  @SequenceGenerator(name = "item_sequence", sequenceName = "item_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_sequence")
  private long itemId;

  @Column(name = "name", nullable = false)
  private String name;

  Item(String name) {
    this.name = name;
  }
}
