package com.pos.features.super_admin.discount.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemDiscountRequest {

    @NotNull(message = "menu id is required")
    private String menuId;

    @NotNull(message = "discount id is required")
    private String discountId;

    @NotNull(message = "creator user id is required")
    private String userId;
}