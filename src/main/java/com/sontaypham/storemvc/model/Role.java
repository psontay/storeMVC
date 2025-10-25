package com.sontaypham.storemvc.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table( name = "roles")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
    @Id
    @EqualsAndHashCode.Exclude
    String name;
    String description;

    @ManyToMany(fetch = FetchType.EAGER , cascade = CascadeType.MERGE)
    @JoinTable( name = "roles_permissions" , joinColumns = @JoinColumn(name = "roles_name") , inverseJoinColumns = @JoinColumn(name = "permissions_name"))
    Set<Permission> permissions;
}
