package com.pos.features.super_admin.menu_n_category.model.entity;

import com.pos.features.super_admin.user.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_category")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(generator = "category-id-generator")
    @GenericGenerator(
            name = "category-id-generator",
            strategy = "com.pos.features.super_admin.menu_n_category.util.CategoryIdGenerator"
    )
    @Column(name = "category_id", length = 5)
    private String categoryId;

    @Column(nullable = false, length = 100)
    private String categoryName;

    @Column(nullable = false)
    private LocalDate createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "user_id", nullable = false)
    private User createdBy;

    @Column(nullable = false)
    private LocalDate updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", referencedColumnName = "user_id",nullable = true)
    private User updatedBy;

    private boolean isDeleted;
}
