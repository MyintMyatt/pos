package com.pos.features.super_admin.tax.util;

import com.pos.features.cashier.sale.model.entity.SalesTax;
import com.pos.features.super_admin.tax.model.response.SaleTaxSimpleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TaxMapper.class})
public interface SaleTaxMapper {

    @Mapping(target = "tax", qualifiedByName = "simpleTaxResponse")
    @Mapping(target = "taxAmount", source = "taxAmount")
    SaleTaxSimpleResponse toSaleTaxSimpleResponse(SalesTax salesTax);

}
