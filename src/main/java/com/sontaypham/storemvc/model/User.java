package com.sontaypham.storemvc.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.UUID;

@Table(name = "users")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @Column( columnDefinition = "uniqueIdentifier")
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Exclude
    UUID id;

    @Column(name = "username")
    String username;
    @Column(name = "password")
    String password;
    @Column(name = "fullName" , columnDefinition = "NVARCHAR(255)")
    String fullName;
    @Column( name = "email")
    String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "user_role" , joinColumns = @JoinColumn( name = "user_id") , inverseJoinColumns =
    @JoinColumn(name = "role_name"))
    Set<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_permission",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_name"))
    Set<Permission> permissions;
}
