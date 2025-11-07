package com.pos.features.cashier.sale.model.response;

import com.pos.features.super_admin.user.model.entity.User;
import com.pos.features.super_admin.user.model.response.UserResponse;
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
    private String saleDate;
    private Double subTotal;
    private Double totalAmount;
    private UserResponse createdBy;
    private String createdDate;
    private UserResponse updatedBy;
    private String updatedDate;
    private List<SalesItemResponse> items;
}
