package com.pos.features.super_admin.menu_n_category.model.response;

import java.time.LocalDate;

public record CategoryResponse(String categoryId, String categoryName, LocalDate createdDate, String createdBy, LocalDate updatedDate, String updatedBy) {
}
