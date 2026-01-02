package com.sontaypham.storemvc.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Table(name = "users")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete( sql = "update users set deleted_at = DATEADD(hour, 7, SYSUTCDATETIME()) where id  = ?")
@SQLRestriction("deleted_at is null")
public class User {
  @Id
  @Column(columnDefinition = "uniqueidentifier")
  @GeneratedValue(strategy = GenerationType.UUID)
  @EqualsAndHashCode.Exclude
  UUID id;

  String username;
  String password;

  @Column( name = "deleted_at")
  LocalDateTime deletedAt;

  @Column(columnDefinition = "NVARCHAR(255)")
  String fullName;

  String email;
  String telPhone;
  String address;

  // rbac
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_role",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_name"))
  Set<Role> roles;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_permission",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "permission_name"))
  Set<Permission> permissions;

  // shop
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  Set<Order> orders;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<PasswordResetToken> passwordResetTokens;
}
