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
public class MenuUpdateRequest {

    @NotBlank(message = "menu name must not be empty or null")
    private String menuName;

    @NotBlank(message = "menu price must not be null")
    private Double price;

    @NotBlank(message = "category id must not be null")
    private String categoryId;

//    @NotBlank(message = "discount must not be null")
//    private boolean isThereDiscount;

    @NotBlank(message = "category desc must not be null or empty")
    private String description;

    @NotNull(message = "updated user must not be null")
    private String updatedBy;
}
