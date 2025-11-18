package com.pos.features.super_admin.menu_n_category.repo;

import com.pos.features.super_admin.menu_n_category.model.entity.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepo extends JpaRepository<MenuItem, String> {
    //    @Query("""
//        SELECT m FROM MenuItem m
//        WHERE m.isDeleted = false
//        AND (:keyword IS NULL OR LOWER(m.menuName AS string) LIKE LOWER(CONCAT('%', :keyword, '%')))
//        AND (:categoryId IS NULL OR m.category.categoryId = :categoryId)
//    """)
    @EntityGraph(
            attributePaths = {
                    "inventory",
                    "menuItemDiscounts.discount",
                    "createdBy",
                    "updatedBy"
            }
    )
    @Query(value = """
            SELECT m FROM MenuItem m
            WHERE m.isDeleted = false
             AND (:keyword IS NULL OR 
             LOWER(CAST(:keyword AS string)) IS NULL OR
             LOWER(m.menuName) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')))
                  AND (:categoryId IS NULL OR m.category.categoryId = :categoryId)
            """)
    Page<MenuItem> searchMenu(
            @Param("keyword") String keyword,
            @Param("categoryId") String categoryId,
            Pageable pageable
    );

//    @Query(value = """
//                SELECT * FROM tbl_menu_item
//                WHERE is_deleted = false
//                  AND (:keyword IS NULL OR LOWER(menu_name::text) LIKE LOWER(CONCAT('%', :keyword, '%')))
//                  AND (:categoryId IS NULL OR fk_category_id = :categoryId)
//            """,
//            nativeQuery = true)
//    Page<MenuItem> searchMenu(
//            @Param("keyword") String keyword,
//            @Param("categoryId") String categoryId,
//            Pageable pageable
//    );
}
