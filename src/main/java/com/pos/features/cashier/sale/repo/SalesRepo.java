package com.pos.features.cashier.sale.repo;

import com.pos.features.cashier.sale.model.entity.Sales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesRepo extends JpaRepository<Sales, String > {

    @Query("SELECT s FROM Sales s " +
            "WHERE LOWER(s.salesId) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(s.createdBy.userName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Sales> searchSales(@Param("keyword") String keyword, Pageable pageable);
}
