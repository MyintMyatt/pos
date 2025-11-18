package com.pos.features.super_admin.menu_n_category.model.response;

import com.pos.features.super_admin.user.model.response.UserSimpleResponse;

import java.io.Serializable;

public record CategoryFullResponse(
        String categoryId,
        String categoryName,
        UserSimpleResponse createdBy,
        String createdDate,
        UserSimpleResponse updatedBy,
        String updatedDate
) implements Serializable {}



