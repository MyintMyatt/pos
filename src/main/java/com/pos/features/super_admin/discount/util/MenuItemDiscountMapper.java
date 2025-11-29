package com.pos.features.super_admin.discount.util;

import com.pos.features.super_admin.discount.model.entity.MenuItemDiscount;
import com.pos.features.super_admin.discount.model.response.MenuItemDiscountResponse;
import com.pos.features.super_admin.user.util.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface MenuItemDiscountMapper {
    @Mapping(target = "discountId", source = "discount.discountId")
    @Mapping(target = "discountValue", source = "discount.discountValue")
    @Mapping(target = "discountType", source = "discount.discountType")
    @Mapping(target = "validFrom", source = "discount.validFrom")
    @Mapping(target = "validTo", source = "discount.validTo")
    @Mapping(target = "createdDate", source = "createdDate")
    @Mapping(target = "createdBy", qualifiedByName = "toSimpleUserResponse")
    MenuItemDiscountResponse toResponse(MenuItemDiscount menuItemDiscount);
}
