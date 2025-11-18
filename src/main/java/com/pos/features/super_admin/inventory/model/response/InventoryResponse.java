package com.pos.features.super_admin.inventory.model.response;

import com.fasterxml.jackson.annotation.JsonView;
import com.pos.common.model.response.ResponseView;
import com.pos.constant.Uom;
import com.pos.features.super_admin.menu_n_category.model.response.MenuSimpleResponse;
import com.pos.features.super_admin.user.model.response.UserSimpleResponse;

public record InventoryResponse(@JsonView(ResponseView.Simple.class) String inventoryId,
                                @JsonView(ResponseView.Simple.class) MenuSimpleResponse menuItem,
                                @JsonView(ResponseView.Simple.class) double quantity,
                                @JsonView(ResponseView.Simple.class) Uom uom,
                                @JsonView(ResponseView.Full.class) String createdDate,
                                @JsonView(ResponseView.Full.class) UserSimpleResponse createdBy,
                                @JsonView(ResponseView.Full.class) String updatedDate,
                                @JsonView(ResponseView.Full.class) UserSimpleResponse updatedBy) {
}


