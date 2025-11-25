package com.pos.features.cashier.sale.util;

import com.pos.features.cashier.sale.model.entity.Sales;
import com.pos.features.cashier.sale.model.response.SalesResponse;
import com.pos.features.super_admin.tax.util.SaleTaxMapper;
import com.pos.features.super_admin.user.util.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { SaleItemMapper.class, UserMapper.class, SaleTaxMapper.class})
public interface SaleMapper {

    @Mapping(target = "salesId", source = "salesId")
    @Mapping(target = "saleDate", source = "saleDate")
    @Mapping(target = "taxs", source = "saleTax")
    @Mapping(target = "subTotal", source = "subTotal")
    @Mapping(target = "totalAmount", source = "totalAmount")
    @Mapping(target = "createdBy", qualifiedByName = "toSimpleUserResponse")
    @Mapping(target = "createdDate", source = "createdDate")
    @Mapping(target = "updatedBy", qualifiedByName = "toSimpleUserResponse")
    @Mapping(target = "updatedDate", source = "updatedDate")
    @Mapping(target = "items", source = "salesItems")
    SalesResponse toResponse(Sales sales);

}
