package com.pos.features.cashier.sale.util;

import com.pos.features.cashier.sale.model.entity.SalesItem;
import com.pos.features.cashier.sale.model.response.SalesItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SaleItemMapper {

    @Mapping(target = "saleItemId", source = "saleItemId")
    @Mapping(target = "menuId", expression = "java(salesItem.getMenuItem().getMenuId())")
    @Mapping(target = "menuName", expression = "java(salesItem.getMenuItem().getMenuName())")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "total", source = "total")
    SalesItemResponse toSaleItemResponse(SalesItem salesItem);
}
