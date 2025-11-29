package com.pos.features.super_admin.menu_n_category.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    @NotBlank(message = "category name must not be empty or null")
    private String categoryName;

    @NotNull(message = "created by must not be null")
    private String actionBy;

}
