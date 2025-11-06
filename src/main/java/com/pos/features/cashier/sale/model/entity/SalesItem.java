package com.pos.features.cashier.sale.model.entity;

import com.pos.features.super_admin.menu_n_category.model.entity.MenuItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_sales_item")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_sales_id", referencedColumnName = "sales_id",nullable = false)
    private Sales sales;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_menu_id", referencedColumnName = "menu_id",nullable = false)
    private MenuItem menuItem;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Double total;

}

