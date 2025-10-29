package com.sontaypham.storemvc.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Table( name = "cart_items")
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
    @EqualsAndHashCode.Exclude
    UUID id;
    @ManyToOne
    @JoinColumn( name = "product_id")
    Product product;
    @ManyToOne
    @JoinColumn( name = "cart_id")
    Cart cart;
    int quantity;
    BigDecimal unitPrice;
}
