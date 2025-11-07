package com.pos.features.super_admin.menu_n_category.model.entity;

import com.pos.features.super_admin.inventory.model.entity.Inventory;
import com.pos.features.super_admin.user.model.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "tbl_menu_item")
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem implements Serializable {

    @Id
    @GeneratedValue(generator = "menu-id-generator")
    @GenericGenerator(
            name = "menu-id-generator",
            strategy = "com.pos.features.super_admin.menu_n_category.util.MenuItemGenerator"
    )
    @Column(name = "menu_id", length = 12)
    private String menuId;

    @Column(nullable = false, length = 50)
    private String menuName;

    @Column(nullable = false)
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_category_id", referencedColumnName = "category_id")
    private Category category;

    private String menuImageUrl;

    @OneToOne
    @JoinColumn(name = "fk_inventory_id", referencedColumnName = "inventory_id")
    private Inventory inventory;

    @Column(nullable = false)
    private boolean isThereDiscount;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "user_id", nullable = false)
    private User createdBy;

    @Column(nullable = true)
    private LocalDate updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", referencedColumnName = "user_id",nullable = true)
    private User updatedBy;

    private boolean isDeleted;

}
