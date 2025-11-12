package com.pos.features.super_admin.menu_n_category.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateRequest {

    @NotEmpty(message = "category id must not be null or empty")
    private String categoryId;

    @NotBlank(message = "category name must not be empty or null")
    private String categoryName;

    @NotNull(message = "created by must not be null")
    private String actionBy;

}
