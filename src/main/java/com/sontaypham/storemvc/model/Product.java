package com.sontaypham.storemvc.model;

import com.sontaypham.storemvc.enums.ProductStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Table(name = "products")
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(columnDefinition = "uniqueidentifier")
  UUID id;
  @Column( columnDefinition = "NVARCHAR(255)")
  String name;
  @Lob String description;
  int age;
  String origin;
  String imageUrl;
  int stockQuantity;
  BigDecimal price;

  @Column(precision = 15, scale = 2)
  BigDecimal originalPrice;

  BigDecimal discountedPrice;
  int discountPercent;

  @Column(updatable = false)
  @org.hibernate.annotations.CreationTimestamp
  LocalDateTime createdAt;

  @org.hibernate.annotations.UpdateTimestamp
  LocalDateTime updatedAt;

  @Enumerated(EnumType.STRING)
  ProductStatus status;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "supplier_id", nullable = false)
  Supplier supplier;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  Set<OrderItem> orderItems = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    Set<Category> categories = new HashSet<>();
}
