package com.pos.features.super_admin.inventory.model.response;

import com.pos.constant.Uom;
import com.pos.features.super_admin.menu_n_category.model.response.MenuSimpleResponse;

public record InventorySimpleResponseWithMenu(
        String inventoryId,
        MenuSimpleResponse menu,
        Integer quantity,
        Uom uom
) {}
