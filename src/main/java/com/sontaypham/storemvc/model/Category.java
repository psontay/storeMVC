package com.sontaypham.storemvc.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

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
    @Column( columnDefinition = "uniqueIdentifier")
    @GeneratedValue( strategy = GenerationType.UUID)
    UUID id;
    @Column( nullable = false , unique = true)
    String name;
    String description;
}
