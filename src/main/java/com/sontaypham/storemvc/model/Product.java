package com.sontaypham.storemvc.model;

import com.sontaypham.storemvc.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

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
    @Column( columnDefinition = "uniqueIdentifier")
    UUID id;
    String name;
    @Lob
    String description;
    int age;
    String origin;
    int stockQuantity;
    BigDecimal price;
    @Column(precision = 15, scale = 2)
    BigDecimal originalPrice;
    @Column(precision = 15, scale = 2)
    int discountPercent;
    @ManyToOne( fetch = FetchType.EAGER)
    Category category;
    @Column(updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    LocalDateTime createdAt;
    @org.hibernate.annotations.UpdateTimestamp
    LocalDateTime updatedAt;
    @Enumerated(EnumType.STRING)
    ProductStatus status;
    @ManyToOne( fetch = FetchType.EAGER)
    @JoinColumn( name = "supplier_id" , nullable = false)
    Supplier supplier;
}
