package com.pos.features.super_admin.inventory.model.response;

import com.pos.constant.Uom;
import com.pos.features.super_admin.menu_n_category.model.entity.MenuItem;
import com.pos.features.super_admin.user.model.entity.User;

import java.time.LocalDate;

public record InventoryResponse(
        String inventoryId,
        MenuItem menuItem,
        double quantity,
        Uom uom,
        LocalDate createdDate,
        User createdBy,
        LocalDate updatedDate,
        User updatedBy
) {
}
