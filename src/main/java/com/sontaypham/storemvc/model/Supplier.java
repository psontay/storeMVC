package com.sontaypham.storemvc.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "suppliers")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Supplier {
    @Id
    @Column( name = "name" , nullable = false)
    String name;
    @Column( name = "fullName" , nullable = false , columnDefinition = "NVARCHAR(255)")
    String fullName;
    @Column( name = "email" , nullable = false)
    String email;
    @Column( name = "telPhone" , nullable = false)
    String telPhone;
    @OneToMany( mappedBy = "supplier" , cascade = CascadeType.ALL ,  fetch = FetchType.EAGER , orphanRemoval = true )
    Set<Product> products;
}
