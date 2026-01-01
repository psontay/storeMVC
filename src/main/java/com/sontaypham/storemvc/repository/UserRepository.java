package com.sontaypham.storemvc.repository;

import com.sontaypham.storemvc.model.User;
import java.util.Optional;
import java.util.UUID;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
  Optional<User> findById(UUID id);

  Optional<User> findByUsername(String username);

  Page<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
      String u, String e, Pageable pageable);

  void deleteByUsername(String username);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);

  Optional<User> findByUsernameOrEmail(String username, String email);

  @Query("""
    select u from User u where lower(u.username) like lower(concat('%' , :username , '%') ) or lower(u.email) like lower(concat( '%' , :email , '%'))
""")
  Page<User> findByUsernameOrEmailContainingIgnoreCase(String username, String email, Pageable pageable);
  // delete feat

    @Query(value = "select * from users where deleted_at is not null", countQuery = "select count (*) from  users " +
                                                                                    "where deleted_at is not null", nativeQuery = true)
    Page<User> findAllDeleted(Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "update users set deleted_at = null where id = :id" , nativeQuery = true)
    void restore(UUID id);
    @Transactional
    @Modifying
    @Query(value = "delete from users where id = :id", nativeQuery = true)
    void hardDelete(UUID id);
}
