package com.pos.features.super_admin.discount.util;

import com.pos.features.super_admin.discount.model.entity.Discount;
import com.pos.features.super_admin.discount.model.request.DiscountRequest;
import com.pos.features.super_admin.discount.model.response.DiscountResponse;
import com.pos.features.super_admin.user.util.UserMapper;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-29T13:52:05+0630",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class DiscountMapperImpl implements DiscountMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Discount toEntity(DiscountRequest request) {
        if ( request == null ) {
            return null;
        }

        Discount.DiscountBuilder discount = Discount.builder();

        discount.discountType( request.getDiscountType() );
        discount.discountValue( request.getDiscountValue() );
        discount.validFrom( request.getValidFrom() );
        discount.validTo( request.getValidTo() );

        discount.isDeleted( false );
        discount.createdDate( LocalDate.now() );

        return discount.build();
    }

    @Override
    public DiscountResponse toResponse(Discount discount) {
        if ( discount == null ) {
            return null;
        }

        DiscountResponse.DiscountResponseBuilder discountResponse = DiscountResponse.builder();

        discountResponse.discountId( discount.getDiscountId() );
        discountResponse.discountType( discount.getDiscountType() );
        discountResponse.discountValue( discount.getDiscountValue() );
        discountResponse.validFrom( discount.getValidFrom() );
        discountResponse.validTo( discount.getValidTo() );
        discountResponse.createdDate( discount.getCreatedDate() );
        discountResponse.createdBy( userMapper.toSimpleResponse( discount.getCreatedBy() ) );
        discountResponse.updatedDate( discount.getUpdatedDate() );
        discountResponse.updatedBy( userMapper.toSimpleResponse( discount.getUpdatedBy() ) );

        return discountResponse.build();
    }
}
