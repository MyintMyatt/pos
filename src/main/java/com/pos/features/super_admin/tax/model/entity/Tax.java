package com.pos.features.super_admin.tax.model.entity;

import com.pos.features.super_admin.user.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_tax")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tax {

    @Id
    @GeneratedValue(generator = "tax-id-generator")
    @GenericGenerator(
            name = "tax-id-generator",
            strategy = "com.pos.features.super_admin.tax.util.TaxIdGenerator"
    )
    @Column(name = "tax_id", length = 12)
    private String taxId;

    @Column(nullable = false)
    private String taxDesc;

    @Column(nullable = false)
    private Double taxPercentage;

    @Column(nullable = false)
    private LocalDate validFromDate;

    private LocalDate validToDate;

    private boolean isActive;

    @Column(nullable = false)
    private LocalDate createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "user_id", nullable = false)
    private User createdBy;

//
//    private LocalDate updatedDate;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "updated_by", referencedColumnName = "user_id",nullable = true)
//    private User updatedBy;

}
