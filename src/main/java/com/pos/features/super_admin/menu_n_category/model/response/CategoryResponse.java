package com.pos.features.super_admin.menu_n_category.model.response;

import com.pos.features.super_admin.user.model.response.UserResponse;
import java.io.Serializable;

public record CategoryResponse(
        String categoryId,
        String categoryName,
        String createdDate,
        UserResponse createdBy,
        String updatedDate,
        UserResponse updatedBy
) implements Serializable {}
