package com.sontaypham.storemvc.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "suppliers")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Exclude
    UUID id;
    @Column( name = "name" , nullable = false)
    String name;
    @Column( name = "fullName" , nullable = false , columnDefinition = "NVARCHAR(255)")
    String fullName;
    @Column( name = "email" , nullable = false)
    String email;
    @Column( name = "telPhone" , nullable = false)
    String telPhone;
    @OneToMany( mappedBy = "supplier" , cascade = CascadeType.ALL ,  fetch = FetchType.LAZY , orphanRemoval = true )
    Set<Product> products;
}
