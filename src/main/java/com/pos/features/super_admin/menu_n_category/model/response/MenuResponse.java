package com.pos.features.super_admin.menu_n_category.model.response;

import com.pos.features.super_admin.menu_n_category.model.entity.Category;
import com.pos.features.super_admin.user.model.entity.User;

import java.time.LocalDate;

public record MenuResponse(String menuId, String menuName, double price, Category category, String imageUrl,
                           boolean isThereDiscount,
                           String description, User createdBy, LocalDate createdDate, User updatedBy,
                           LocalDate updatedDate) {
}
