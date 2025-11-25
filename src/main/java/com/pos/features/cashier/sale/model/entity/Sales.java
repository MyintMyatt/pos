package com.pos.features.cashier.sale.model.entity;

import com.pos.features.super_admin.user.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tbl_sales")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sales {

    @Id
    @GeneratedValue(generator = "sales-id-generator")
    @GenericGenerator(
            name = "sales-id-generator",
            strategy = "com.pos.features.cashier.sale.util.SalesIdGenerator"
    )
    @Column(name = "sales_id", length = 15)
    private String salesId;

    @Column(nullable = false)
    private LocalDate saleDate;

    @Column(nullable = false)
    private Double subTotal;

    @Column(nullable = false)
    private Double totalAmount;

    @OneToMany(mappedBy = "sales", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SalesItem> salesItems;

    @OneToMany(mappedBy = "sales", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SalesTax> saleTax;

    @Column(nullable = false)
    private LocalDate createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "user_id", nullable = false)
    private User createdBy;

    private LocalDate updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", referencedColumnName = "user_id",nullable = true)
    private User updatedBy;

}
