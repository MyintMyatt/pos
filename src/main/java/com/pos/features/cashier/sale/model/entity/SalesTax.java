package com.pos.features.cashier.sale.model.entity;

import com.pos.features.super_admin.tax.model.entity.Tax;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_sales_tax")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesTax {

    @EmbeddedId
    private SaleTaxId saleTaxId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("salesId")
    @JoinColumn(name = "fk_sales_id", referencedColumnName = "sales_id",nullable = false)
    private Sales sales;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("taxId")
    @JoinColumn(name = "fk_tax_id", referencedColumnName = "tax_id",nullable = false)
    private Tax tax;

    @Column(nullable = false)
    private Double taxAmount;
}
