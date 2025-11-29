package com.pos.features.super_admin.menu_n_category.model.response;

public record MenuSimpleResponse(
        String menuId,
        String menuName,
        CategorySimpleResponse category
) {
}
