package com.pos.features.super_admin.inventory.model.entity;

import com.pos.constant.Uom;
import com.pos.features.super_admin.menu_n_category.model.entity.MenuItem;
import com.pos.features.super_admin.user.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_inventory")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
    @Id
    @GeneratedValue(generator = "inventory-id-generator")
    @GenericGenerator(
            name = "inventory-id-generator",
            strategy = "com.pos.features.super_admin.inventory.util.InventoryIdGenerator"
    )
    @Column(name = "inventory_id", length = 15)
    private String inventoryId;

    @OneToOne
    @JoinColumn(name = "fk_menu_id", referencedColumnName = "menu_id", nullable = false, unique = true)
    private MenuItem menuItem;

    @Column(nullable = false)
    private double quantity= 0.0;

    @Enumerated(EnumType.STRING)
    private Uom uom;

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
}
