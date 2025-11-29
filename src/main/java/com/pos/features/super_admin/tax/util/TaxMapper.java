package com.pos.features.super_admin.tax.util;

import com.pos.features.cashier.sale.model.entity.SalesTax;
import com.pos.features.super_admin.tax.model.entity.Tax;
import com.pos.features.super_admin.tax.model.response.TaxSimpleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface TaxMapper {

    @Named("simpleTaxResponse")
    @Mapping(target = "taxId", source = "taxId")
    @Mapping(target = "taxDesc", source = "taxDesc")
    @Mapping(target = "taxPercentage", source = "taxPercentage")
    TaxSimpleResponse toSimpleTaxResponse(Tax tax);

}
