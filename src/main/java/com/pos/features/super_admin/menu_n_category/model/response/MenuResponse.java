package com.pos.features.super_admin.menu_n_category.model.response;

import com.fasterxml.jackson.annotation.JsonView;
import com.pos.common.model.response.ResponseView;
import com.pos.features.super_admin.discount.model.response.MenuItemDiscountResponse;
import com.pos.features.super_admin.inventory.model.response.InventoryResponse;
import com.pos.features.super_admin.inventory.model.response.InventorySimpleResponse;
import com.pos.features.super_admin.user.model.response.UserResponse;
import com.pos.features.super_admin.user.model.response.UserSimpleResponse;

import java.io.Serializable;
import java.util.List;

public record MenuResponse(
        @JsonView(ResponseView.Simple.class)
        String menuId,

        @JsonView(ResponseView.Simple.class)
        String menuName,

        @JsonView(ResponseView.Full.class)
        double price,

        @JsonView(ResponseView.Full.class)
        CategorySimpleResponse category,

        @JsonView(ResponseView.Full.class)
        String imageUrl,

        @JsonView(ResponseView.Full.class)
        boolean isThereDiscount,

        @JsonView(ResponseView.Full.class)
        String description,

        @JsonView(ResponseView.Full.class)
        UserSimpleResponse createdBy,

        @JsonView(ResponseView.Full.class)
        String createdDate,

        @JsonView(ResponseView.Full.class)
        UserSimpleResponse updatedBy,

        @JsonView(ResponseView.Full.class)
        String updatedDate,

        @JsonView(ResponseView.Simple.class)
        InventorySimpleResponse inventory,

        @JsonView(ResponseView.Full.class)
        List<MenuItemDiscountResponse> discounts) implements Serializable {
}
