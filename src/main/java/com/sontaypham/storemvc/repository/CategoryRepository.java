package com.sontaypham.storemvc.repository;

import com.sontaypham.storemvc.model.Category;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {}
