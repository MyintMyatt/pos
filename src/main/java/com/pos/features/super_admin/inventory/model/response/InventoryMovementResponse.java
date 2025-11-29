package com.pos.features.super_admin.inventory.model.response;

import com.pos.constant.InventoryMovementType;
import com.pos.features.super_admin.user.model.response.UserSimpleResponse;

public record InventoryMovementResponse(
        long inventoryMovementId,
        int beforeQty,
        int afterQty,
        int quantityChange,
        InventoryMovementType inventoryMovementType,
        InventorySimpleResponseWithMenu  currentInventoryStock,
        String createdDate,
        UserSimpleResponse createdBy
) {
}
