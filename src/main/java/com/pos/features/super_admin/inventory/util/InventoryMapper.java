package com.pos.features.super_admin.inventory.util;

import com.pos.constant.Uom;
import com.pos.features.super_admin.inventory.model.entity.Inventory;
import com.pos.features.super_admin.inventory.model.entity.InventoryMovement;
import com.pos.features.super_admin.inventory.model.response.InventoryMovementResponse;
import com.pos.features.super_admin.inventory.model.response.InventoryResponse;
import com.pos.features.super_admin.inventory.model.response.InventorySimpleResponse;
import com.pos.features.super_admin.inventory.model.response.InventorySimpleResponseWithMenu;
import com.pos.features.super_admin.menu_n_category.model.response.MenuSimpleResponse;
import com.pos.features.super_admin.menu_n_category.util.MenuMapper;
import com.pos.features.super_admin.user.util.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {MenuMapper.class, UserMapper.class})
public interface InventoryMapper {

//    InventoryMapper INSTANCE = Mappers.getMapper(InventoryMapper.class);

    @Named("toFullInvResponse")
    @Mapping(target = "inventoryId", source = "inventoryId")
    @Mapping(target = "menuItem", qualifiedByName = "toSimpleMenuResponse")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "uom", source = "uom")
    @Mapping(target = "createdDate", expression = "java(inventory.getCreatedDate().toString())")
    @Mapping(target = "createdBy", qualifiedByName = "toSimpleUserResponse")
    @Mapping(target = "updatedDate",expression = "java(inventory.getUpdatedDate() != null ? inventory.getUpdatedDate().toString() : null)")
    @Mapping(target = "updatedBy",qualifiedByName = "toSimpleUserResponse")
    InventoryResponse toFullResponse(Inventory inventory);

    @Named("toSimpleInvResponse")
    @Mapping(target = "inventoryId", source = "inventoryId")
    @Mapping(target = "menu",source = "menuItem", qualifiedByName = "toSimpleMenuResponse")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "uom", source = "uom")
    InventorySimpleResponseWithMenu toSimpleResponse(Inventory inventory);
//    @Mapping(target = "menuItem", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)


    @Mapping(target = "inventoryMovementId", source = "inventoryMovementId")
    @Mapping(target = "beforeQty", source = "beforeQty")
    @Mapping(target = "afterQty", source = "afterQty")
    @Mapping(target = "quantityChange", source = "quantityChange")
    @Mapping(target = "inventoryMovementType", source = "inventoryMovementType")
    @Mapping(target = "currentInventoryStock",source = "inventory", qualifiedByName = "toSimpleInvResponse")
    @Mapping(target = "createdDate", source = "createdDate")
    @Mapping(target = "createdBy", qualifiedByName = "toSimpleUserResponse")
    InventoryMovementResponse toFullInvMovementResponse(InventoryMovement inventoryMovement);


}
