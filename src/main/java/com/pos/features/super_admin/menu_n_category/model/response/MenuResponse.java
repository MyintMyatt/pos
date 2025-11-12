package com.pos.features.super_admin.menu_n_category.model.response;

import com.pos.features.super_admin.discount.model.response.MenuItemDiscountResponse;
import com.pos.features.super_admin.inventory.model.response.InventoryCustomResponse;
import com.pos.features.super_admin.inventory.model.response.InventoryResponse;
import com.pos.features.super_admin.menu_n_category.model.entity.Category;
import com.pos.features.super_admin.user.model.entity.User;
import com.pos.features.super_admin.user.model.response.UserResponse;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public record MenuResponse(String menuId, String menuName, double price, CategoryResponse category, String imageUrl,
                           boolean isThereDiscount,
                           String description, UserResponse createdBy, String createdDate, UserResponse updatedBy,
                           String updatedDate,
                           InventoryCustomResponse inventory,
                           List<MenuItemDiscountResponse> discounts) implements Serializable {
}
