package com.pos.features.cashier.sale.repo;

import com.pos.features.cashier.sale.model.entity.Sales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface SalesRepo extends JpaRepository<Sales, String> {

//    @Query("SELECT s FROM Sales s " +
//            "WHERE LOWER(s.salesId) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
//            "OR LOWER(s.createdBy.userName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
//    Page<Sales> searchSales(@Param("keyword") String keyword, Pageable pageable);

    //    @Query("""
//    SELECT s FROM Sales s
//    WHERE
//        (:keyword IS NULL OR LOWER(s.salesId) LIKE LOWER(CONCAT('%', :keyword, '%'))
//         OR LOWER(s.createdBy.userName) LIKE LOWER(CONCAT('%', :keyword, '%')))
//      AND (:startDate IS NULL OR s.saleDate >= :startDate)
//      AND (:endDate IS NULL OR s.saleDate <= :endDate)
//""")
//    Page<Sales> searchSales(
//            @Param("keyword") String keyword,
//            @Param("startDate") LocalDate startDate,
//            @Param("endDate") LocalDate endDate,
//            Pageable pageable
//    );
//    @Query(value = """
//                SELECT * FROM tbl_sales
//                WHERE (:keyword IS NULL OR LOWER(sales_id::text) LIKE LOWER(CONCAT('%', :keyword, '%')))
//                  AND ( (:startDate IS NULL OR :endDate IS NULL) OR sale_date BETWEEN :startDate AND :endDate)
//            """, nativeQuery = true)
//    Page<Sales> searchSales(
//            @Param("keyword") String keyword,
//            @Param("startDate") LocalDate startDate,
//            @Param("endDate") LocalDate endDate,
//            Pageable pageable
//    );

    @Query("""
                SELECT s FROM Sales s
                WHERE (:keyword IS NULL OR LOWER(CAST(s.salesId AS string)) LIKE LOWER(CONCAT('%', :keyword, '%')))
                  AND (:startDate IS NULL OR s.saleDate >= :startDate)
                  AND (:endDate IS NULL OR s.saleDate <= :endDate)
            """)
    Page<Sales> searchSales(
            @Param("keyword") String keyword,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
            , Pageable pageable
    );


}
