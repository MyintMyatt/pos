package com.pos.features.super_admin.inventory.model.response;

import com.pos.constant.Uom;

import java.time.LocalDate;

public record InventoryCustomResponse
        (String inventoryId, double quantity, Uom uom) {
}
