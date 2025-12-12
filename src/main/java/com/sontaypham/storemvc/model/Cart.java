package com.sontaypham.storemvc.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cart {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(columnDefinition = "uniqueidentifier")
  UUID id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  User user;

  @OneToMany(
      mappedBy = "cart",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER)
  @OrderBy("id")
  @Builder.Default
  List<CartItem> items = new ArrayList<>();

  @Column(precision = 18, scale = 2)
  BigDecimal totalPrice;

  public void recalcTotal() {
    this.totalPrice =
        items.stream()
            .map(
                item -> {
                  if (item.getSubtotal() == null) item.recalcSubtotal();
                  return item.getSubtotal();
                })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public void addItem(CartItem item) {
    item.setCart(this);
    items.add(item);
    recalcTotal();
  }

  public void removeItem(CartItem item) {
    items.remove(item);
    item.setCart(null);
    recalcTotal();
  }
}
