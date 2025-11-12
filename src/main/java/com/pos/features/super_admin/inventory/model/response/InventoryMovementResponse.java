package com.pos.features.super_admin.inventory.model.response;

import com.pos.constant.InventoryMovementType;
import com.pos.features.super_admin.user.model.response.UserResponse;

public record InventoryMovementResponse(
        
        long inventoryMovementId,
        int quantityChange,
        InventoryMovementType inventoryMovementType,
        InventoryResponse inventoryResponse,
        String createdDate,
        UserResponse createdBy


) {
}
