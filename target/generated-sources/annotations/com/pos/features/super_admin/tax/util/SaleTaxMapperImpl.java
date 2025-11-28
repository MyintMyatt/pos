package com.pos.features.super_admin.tax.util;

import com.pos.features.cashier.sale.model.entity.SalesTax;
import com.pos.features.super_admin.tax.model.response.SaleTaxSimpleResponse;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-28T13:39:22+0630",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class SaleTaxMapperImpl implements SaleTaxMapper {

    @Autowired
    private TaxMapper taxMapper;

    @Override
    public SaleTaxSimpleResponse toSaleTaxSimpleResponse(SalesTax salesTax) {
        if ( salesTax == null ) {
            return null;
        }

        SaleTaxSimpleResponse saleTaxSimpleResponse = new SaleTaxSimpleResponse();

        saleTaxSimpleResponse.setTax( taxMapper.toSimpleTaxResponse( salesTax.getTax() ) );
        if ( salesTax.getTaxAmount() != null ) {
            saleTaxSimpleResponse.setTaxAmount( salesTax.getTaxAmount() );
        }

        return saleTaxSimpleResponse;
    }
}
