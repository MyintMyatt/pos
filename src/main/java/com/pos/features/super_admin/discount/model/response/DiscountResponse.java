package com.pos.features.super_admin.discount.model.response;


import com.pos.constant.DiscountType;
import com.pos.features.super_admin.user.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountResponse {

    private String discountId;
    private DiscountType discountType;
    private Double discountValue;
    private LocalDate validFrom;
    private LocalDate validTo;
    private LocalDate createdDate;
    private User createdBy;
    private LocalDate updatedDate;
    private User updatedBy;

}
