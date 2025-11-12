package com.pos.features.super_admin.inventory.model.request;

import com.pos.constant.InventoryMovementType;
import com.pos.constant.Uom;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryMovementRequest {

    @NotBlank(message = "menu id must not be null or empty")
    private String menuId;

    @NotNull(message = "inventory movement type must not be null")
    private InventoryMovementType movementType;

    @Min(value = 0, message = "quantity must not be minus")
    private Integer quantity;

    @NotNull(message = "UOM must not be null")
    private Uom uom;

    @NotBlank(message = "created by (user_id) must not be null or empty")
    private String createdBy;

}
