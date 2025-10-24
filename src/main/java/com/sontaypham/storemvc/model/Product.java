package com.sontaypham.storemvc.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    UUID id;
    @Column( name = "name")
    String name;
    @ManyToOne( fetch = FetchType.EAGER)
    @JoinColumn( name = "supplier_name" , nullable = false)
    Supplier supplier;
}
