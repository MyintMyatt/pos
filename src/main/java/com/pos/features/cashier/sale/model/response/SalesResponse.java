package com.pos.features.cashier.sale.model.response;

import com.pos.features.super_admin.tax.model.response.SaleTaxSimpleResponse;
import com.pos.features.super_admin.user.model.response.UserSimpleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesResponse {
    private String salesId;
    private String saleDate;
    private List<SaleTaxSimpleResponse> taxs;
    private Double subTotal;
    private Double totalAmount;
    private UserSimpleResponse createdBy;
    private String createdDate;
    private UserSimpleResponse updatedBy;
    private String updatedDate;
    private List<SalesItemResponse> items;
}
