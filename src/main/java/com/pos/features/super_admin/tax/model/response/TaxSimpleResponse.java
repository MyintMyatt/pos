package com.pos.features.super_admin.tax.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxSimpleResponse {
    private String taxId;
    private String taxDesc;
    private Double taxPercentage;
}
