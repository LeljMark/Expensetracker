package com.markoleljak.expensetracker.repository;

import com.markoleljak.expensetracker.model.Category;
import com.markoleljak.expensetracker.model.Expense;
import com.markoleljak.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserOrderByDateDesc(User user);

    @Query("SELECT e FROM Expense e WHERE e.user = :user AND e.date <= :end AND e.date >= :start " +
            "ORDER BY e.date DESC")
    List<Expense> findByUserAndDateBetweenOrderByDateDesc(@Param("user") User user, @Param("start") LocalDate start,
                                                          @Param("end") LocalDate end);

    @Query("SELECT e FROM Expense e WHERE e.user = :user AND e.date >= :start ORDER BY e.date DESC")
    List<Expense> findByUserAndAfterDateOrderByDateDesc(@Param("user") User user, @Param("start") LocalDate start);

    @Query("SELECT e FROM Expense e WHERE e.user = :user AND e.date <= :end ORDER BY e.date DESC")
    List<Expense> findByUserAndBeforeDateOrderByDateDesc(@Param("user") User user, @Param("end") LocalDate end);

    List<Expense> findByUserAndCategory(User user, Category category);


}
