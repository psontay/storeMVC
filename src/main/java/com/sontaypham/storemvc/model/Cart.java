package com.sontaypham.storemvc.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
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

  @OneToOne
  @JoinColumn(name = "user_id")
  User user;

  @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
  Set<CartItem> items;

  BigDecimal totalPrice;
}
