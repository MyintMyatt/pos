package com.pos.features.super_admin.menu_n_category.model.response;

import com.pos.features.super_admin.discount.model.response.MenuItemDiscountResponse;
import com.pos.features.super_admin.menu_n_category.model.entity.Category;
import com.pos.features.super_admin.user.model.entity.User;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public record MenuResponse(String menuId, String menuName, double price, CategoryResponse category, String imageUrl,
                           boolean isThereDiscount,
                           String description, User createdBy, String createdDate, User updatedBy,
                           String updatedDate, List<MenuItemDiscountResponse> discounts) implements Serializable {
}
