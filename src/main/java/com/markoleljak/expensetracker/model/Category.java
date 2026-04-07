package com.markoleljak.expensetracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Category entity representing an expense category in the application. Contains a unique name for the category.
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
