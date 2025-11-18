package com.pos.features.super_admin.inventory.model.response;

import com.pos.constant.InventoryMovementType;
import com.pos.features.super_admin.user.model.response.UserResponse;
import com.pos.features.super_admin.user.model.response.UserSimpleResponse;

public record InventoryMovementResponse(
        long inventoryMovementId,
        int quantityChange,
        InventoryMovementType inventoryMovementType,
        InventorySimpleResponse currentInventoryStock,
        String createdDate,
        UserSimpleResponse createdBy
) {
}
