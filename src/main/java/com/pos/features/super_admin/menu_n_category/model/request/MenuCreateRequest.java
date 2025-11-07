package com.pos.features.super_admin.menu_n_category.model.request;

import com.pos.constant.InventoryMovementType;
import com.pos.constant.Uom;
import jakarta.validation.constraints.Min;
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
public class MenuCreateRequest {

    @NotBlank(message = "menu name must not be empty or null")
    private String menuName;

    @NotNull(message = "menu price must not be null")
    private Double price;

    @NotBlank(message = "category id must not be null")
    private String categoryId;

    @NotBlank(message = "category desc must not be null or empty")
    private String description;

    @NotNull(message = "created user must not be null")
    private String createdBy;

    @NotNull(message = "inventory movement type must not be null")
    private InventoryMovementType movementType;

    @Min(value = 0, message = "quantity must not be minus")
    private int quantity;

    @NotNull(message = "UOM must not be null")
    private Uom uom;

}
