package com.sontaypham.storemvc.model;

import com.sontaypham.storemvc.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table( name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults( level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue ( strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne
    @JoinColumn( name = "user_id")
    User user;
    @Column(updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    LocalDateTime orderDate;
    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;
    String shippingAddress;
    BigDecimal totalPrice;

    @OneToMany ( mappedBy = "order" , cascade = CascadeType.ALL)
    Set<OrderItem> orderItems;
}
