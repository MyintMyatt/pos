package com.pos.features.cashier.sale.util;

import com.pos.features.cashier.sale.model.entity.Sales;
import com.pos.features.cashier.sale.model.entity.SalesItem;
import com.pos.features.cashier.sale.model.entity.SalesTax;
import com.pos.features.cashier.sale.model.response.SalesItemResponse;
import com.pos.features.cashier.sale.model.response.SalesResponse;
import com.pos.features.super_admin.tax.model.response.SaleTaxSimpleResponse;
import com.pos.features.super_admin.tax.util.SaleTaxMapper;
import com.pos.features.super_admin.user.util.UserMapper;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-28T14:17:10+0630",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class SaleMapperImpl implements SaleMapper {

    @Autowired
    private SaleItemMapper saleItemMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SaleTaxMapper saleTaxMapper;

    @Override
    public SalesResponse toResponse(Sales sales) {
        if ( sales == null ) {
            return null;
        }

        SalesResponse.SalesResponseBuilder salesResponse = SalesResponse.builder();

        salesResponse.salesId( sales.getSalesId() );
        if ( sales.getSaleDate() != null ) {
            salesResponse.saleDate( DateTimeFormatter.ISO_LOCAL_DATE.format( sales.getSaleDate() ) );
        }
        salesResponse.taxs( salesTaxListToSaleTaxSimpleResponseList( sales.getSaleTax() ) );
        salesResponse.subTotal( sales.getSubTotal() );
        salesResponse.totalAmount( sales.getTotalAmount() );
        salesResponse.createdBy( userMapper.toSimpleResponse( sales.getCreatedBy() ) );
        if ( sales.getCreatedDate() != null ) {
            salesResponse.createdDate( DateTimeFormatter.ISO_LOCAL_DATE.format( sales.getCreatedDate() ) );
        }
        salesResponse.updatedBy( userMapper.toSimpleResponse( sales.getUpdatedBy() ) );
        if ( sales.getUpdatedDate() != null ) {
            salesResponse.updatedDate( DateTimeFormatter.ISO_LOCAL_DATE.format( sales.getUpdatedDate() ) );
        }
        salesResponse.items( salesItemListToSalesItemResponseList( sales.getSalesItems() ) );

        return salesResponse.build();
    }

    protected List<SaleTaxSimpleResponse> salesTaxListToSaleTaxSimpleResponseList(List<SalesTax> list) {
        if ( list == null ) {
            return null;
        }

        List<SaleTaxSimpleResponse> list1 = new ArrayList<SaleTaxSimpleResponse>( list.size() );
        for ( SalesTax salesTax : list ) {
            list1.add( saleTaxMapper.toSaleTaxSimpleResponse( salesTax ) );
        }

        return list1;
    }

    protected List<SalesItemResponse> salesItemListToSalesItemResponseList(List<SalesItem> list) {
        if ( list == null ) {
            return null;
        }

        List<SalesItemResponse> list1 = new ArrayList<SalesItemResponse>( list.size() );
        for ( SalesItem salesItem : list ) {
            list1.add( saleItemMapper.toSaleItemResponse( salesItem ) );
        }

        return list1;
    }
}
