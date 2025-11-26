package com.pos.features.super_admin.tax.util;

import com.pos.features.super_admin.tax.model.entity.Tax;
import com.pos.features.super_admin.tax.model.response.TaxSimpleResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-26T15:03:35+0630",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class TaxMapperImpl implements TaxMapper {

    @Override
    public TaxSimpleResponse toSimpleTaxResponse(Tax tax) {
        if ( tax == null ) {
            return null;
        }

        TaxSimpleResponse taxSimpleResponse = new TaxSimpleResponse();

        taxSimpleResponse.setTaxId( tax.getTaxId() );
        taxSimpleResponse.setTaxDesc( tax.getTaxDesc() );
        taxSimpleResponse.setTaxPercentage( tax.getTaxPercentage() );

        return taxSimpleResponse;
    }
}
