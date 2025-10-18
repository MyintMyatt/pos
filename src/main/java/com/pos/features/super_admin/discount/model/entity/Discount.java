package com.pos.features.super_admin.discount.model.entity;

import com.pos.constant.DiscountType;
import com.pos.features.super_admin.user.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_discount")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Discount {

    @Id
    @GeneratedValue(generator = "dis-id-generator")
    @GenericGenerator(
            name = "dis-id-generator",
            strategy = "com.pos.features.super_admin.discount.util.DiscountIdGenerator"
    )
    @Column(name = "discount_id", length = 12)
    private String discountId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType;

    @Column(nullable = false)
    private Double discountValue;

    @Column(nullable = false)
    private LocalDate validFrom;

    @Column(nullable = false)
    private LocalDate validTo;

    @Column(nullable = false)
    private boolean isDeleted;

    private LocalDate deletedDate;

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
