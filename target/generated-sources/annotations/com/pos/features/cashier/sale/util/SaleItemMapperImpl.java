package com.pos.features.cashier.sale.util;

import com.pos.features.cashier.sale.model.entity.SalesItem;
import com.pos.features.cashier.sale.model.response.SalesItemResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-26T10:53:42+0630",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class SaleItemMapperImpl implements SaleItemMapper {

    @Override
    public SalesItemResponse toSaleItemResponse(SalesItem salesItem) {
        if ( salesItem == null ) {
            return null;
        }

        SalesItemResponse.SalesItemResponseBuilder salesItemResponse = SalesItemResponse.builder();

        salesItemResponse.saleItemId( salesItem.getSaleItemId() );
        salesItemResponse.quantity( salesItem.getQuantity() );
        salesItemResponse.price( salesItem.getPrice() );
        salesItemResponse.total( salesItem.getTotal() );

        salesItemResponse.menuId( salesItem.getMenuItem().getMenuId() );
        salesItemResponse.menuName( salesItem.getMenuItem().getMenuName() );

        return salesItemResponse.build();
    }
}
