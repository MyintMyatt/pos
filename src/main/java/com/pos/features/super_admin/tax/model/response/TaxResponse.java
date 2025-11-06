package com.pos.features.super_admin.tax.model.response;


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
public class TaxResponse {

    private String taxId;
    private String taxDesc;
    private Double taxPercentage;
    private LocalDate validFrom;
    private LocalDate validTo;
    private LocalDate createdDate;
    private User createdBy;
    private LocalDate updatedDate;
    private User updatedBy;

}
