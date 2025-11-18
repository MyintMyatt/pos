package com.pos.features.super_admin.menu_n_category.util;

import com.pos.features.super_admin.inventory.model.entity.Inventory;
import com.pos.features.super_admin.inventory.model.response.InventoryResponse;
import com.pos.features.super_admin.inventory.model.response.InventorySimpleResponse;
import com.pos.features.super_admin.inventory.util.InventoryMapper;
import com.pos.features.super_admin.menu_n_category.model.entity.MenuItem;
import com.pos.features.super_admin.menu_n_category.model.request.MenuRequest;
import com.pos.features.super_admin.menu_n_category.model.response.MenuResponse;
import com.pos.features.super_admin.menu_n_category.model.response.MenuSimpleResponse;
import com.pos.features.super_admin.user.util.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CategoryMapper.class}, imports = {java.time.LocalDate.class})
public interface MenuMapper {

    @Named("toFullMenuResponse")
    @Mapping(target = "menuId", source = "menuId")
    @Mapping(target = "menuName", source = "menuName")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "category", qualifiedByName = "toSimpleCategoryResponse")
    @Mapping(target = "imageUrl", source = "menuImageUrl")
    @Mapping(target = "isThereDiscount", source = "thereDiscount")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "createdBy", qualifiedByName = "toSimpleUserResponse")
    @Mapping(target = "createdDate", source = "createdDate")
    @Mapping(target = "updatedBy", qualifiedByName = "toSimpleUserResponse")
    @Mapping(target = "updatedDate", source = "updatedDate")
    @Mapping(target = "inventory", expression = "java(toInvSimpleResponse(menuItem.getInventory()))")
    @Mapping(target = "discounts", source = "menuItemDiscounts")
    MenuResponse toFullResponse(MenuItem menuItem);

    @Named("toSimpleMenuResponse")
    @Mapping(target = "menuId", source = "menuId")
    @Mapping(target = "menuName", source = "menuName")
    @Mapping(target = "category", qualifiedByName = "toSimpleCategoryResponse")
    MenuSimpleResponse toSimpleResponse(MenuItem menuItem);
//    @Mapping(target = "price", ignore = true)
//    @Mapping(target = "imageUrl", ignore = true)
//    @Mapping(target = "isThereDiscount", ignore = true)
//    @Mapping(target = "description", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "inventory", ignore = true)
//    @Mapping(target = "discounts", ignore = true)


    @Mapping(target = "menuId", ignore = true)
    @Mapping(target = "menuName", source = "menuName")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "category",ignore = true)
    @Mapping(target = "inventory", expression = "java(toInventory(request))")
    @Mapping(target = "menuItemDiscounts", ignore = true)
    @Mapping(target = "isThereDiscount", constant = "false")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "createdDate", expression = "java(LocalDate.now())")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    MenuItem toEntity(MenuRequest request);

    default Inventory toInventory(MenuRequest request) {
        Inventory inv = new Inventory();
        inv.setQuantity(request.getQuantity());
        inv.setUom(request.getUom());
        inv.setCreatedDate(LocalDate.now());
        return inv;
    }

    default InventorySimpleResponse toInvSimpleResponse(Inventory inventory) {
        if (inventory == null) return null;
        InventorySimpleResponse res = new InventorySimpleResponse(
                inventory.getInventoryId(),
                inventory.getQuantity(),
                inventory.getUom()
        );
        return res;
    }


}
