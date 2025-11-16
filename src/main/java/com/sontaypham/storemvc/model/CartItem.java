package com.sontaypham.storemvc.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Table(name = "cart_items")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItem {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(columnDefinition = "uniqueidentifier")
  UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "product_id")
  Product product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cart_id")
  Cart cart;

  int quantity;

  @Column(precision = 15, scale = 2)
  BigDecimal unitPrice;

  @Column(precision = 18, scale = 2)
  BigDecimal subtotal;

  public void recalcSubtotal() {
    if (unitPrice == null) unitPrice = BigDecimal.ZERO;
    this.subtotal = unitPrice.multiply(BigDecimal.valueOf(Math.max(0, quantity)));
  }
}
