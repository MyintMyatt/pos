package com.pos.features.cashier.sale.model.response;

import com.pos.features.super_admin.user.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesResponse {
    private String salesId;
    private LocalDate saleDate;
    private Double subTotal;
    private Double totalAmount;
    private User createdBy;
    private LocalDate createdDate;
    private User updatedBy;
    private LocalDate updatedDate;
    private List<SalesItemResponse> items;
}
