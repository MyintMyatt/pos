package com.pos.features.super_admin.discount.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemDiscountResponse {
    private Long id;
    private String menuId;
    private String menuName;
    private String discountId;
    private Double discountValue;
    private String discountType;
    private LocalDate validFrom;
    private LocalDate validTo;
    private LocalDate createdDate;
    private String createdBy;
}
