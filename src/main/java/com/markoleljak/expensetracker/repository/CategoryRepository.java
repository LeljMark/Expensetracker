package com.markoleljak.expensetracker.repository;

import com.markoleljak.expensetracker.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing Category entities. Provides a method to retrieve categories based on their name.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
