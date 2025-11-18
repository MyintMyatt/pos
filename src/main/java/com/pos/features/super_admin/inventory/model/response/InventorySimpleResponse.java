package com.pos.features.super_admin.inventory.model.response;

import com.pos.constant.Uom;

public record InventorySimpleResponse(
        String inventoryId,
        Integer quantity,
        Uom uom
) {}
