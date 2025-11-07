package com.pos.features.super_admin.inventory.model.response;

import com.pos.constant.Uom;
import com.pos.features.super_admin.menu_n_category.model.entity.MenuItem;
import com.pos.features.super_admin.menu_n_category.model.response.MenuResponse;
import com.pos.features.super_admin.user.model.entity.User;
import com.pos.features.super_admin.user.model.response.UserResponse;

import java.time.LocalDate;

public record InventoryResponse(String inventoryId, MenuResponse menuItem, double quantity, Uom uom,
                                String createdDate, UserResponse createdBy, String updatedDate,
                                UserResponse updatedBy) {
}


