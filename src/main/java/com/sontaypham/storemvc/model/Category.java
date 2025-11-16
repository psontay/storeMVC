package com.sontaypham.storemvc.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {
  @Id
  @Column(columnDefinition = "uniqueidentifier")
  @GeneratedValue(strategy = GenerationType.UUID)
  UUID id;

  @Column(nullable = false, unique = true)
  String name;

  String description;

  @ManyToMany(mappedBy = "categories")
  Set<Product> products = new HashSet<>();
}
