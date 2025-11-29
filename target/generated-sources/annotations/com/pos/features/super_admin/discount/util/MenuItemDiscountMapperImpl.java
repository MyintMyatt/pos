package com.pos.features.super_admin.discount.util;

import com.pos.constant.DiscountType;
import com.pos.features.super_admin.discount.model.entity.Discount;
import com.pos.features.super_admin.discount.model.entity.MenuItemDiscount;
import com.pos.features.super_admin.discount.model.response.MenuItemDiscountResponse;
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
public class MenuItemDiscountMapperImpl implements MenuItemDiscountMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public MenuItemDiscountResponse toResponse(MenuItemDiscount menuItemDiscount) {
        if ( menuItemDiscount == null ) {
            return null;
        }

        MenuItemDiscountResponse.MenuItemDiscountResponseBuilder menuItemDiscountResponse = MenuItemDiscountResponse.builder();

        menuItemDiscountResponse.discountId( menuItemDiscountDiscountDiscountId( menuItemDiscount ) );
        menuItemDiscountResponse.discountValue( menuItemDiscountDiscountDiscountValue( menuItemDiscount ) );
        DiscountType discountType = menuItemDiscountDiscountDiscountType( menuItemDiscount );
        if ( discountType != null ) {
            menuItemDiscountResponse.discountType( discountType.name() );
        }
        menuItemDiscountResponse.validFrom( menuItemDiscountDiscountValidFrom( menuItemDiscount ) );
        menuItemDiscountResponse.validTo( menuItemDiscountDiscountValidTo( menuItemDiscount ) );
        menuItemDiscountResponse.createdDate( menuItemDiscount.getCreatedDate() );
        menuItemDiscountResponse.createdBy( userMapper.toSimpleResponse( menuItemDiscount.getCreatedBy() ) );

        return menuItemDiscountResponse.build();
    }

    private String menuItemDiscountDiscountDiscountId(MenuItemDiscount menuItemDiscount) {
        Discount discount = menuItemDiscount.getDiscount();
        if ( discount == null ) {
            return null;
        }
        return discount.getDiscountId();
    }

    private Double menuItemDiscountDiscountDiscountValue(MenuItemDiscount menuItemDiscount) {
        Discount discount = menuItemDiscount.getDiscount();
        if ( discount == null ) {
            return null;
        }
        return discount.getDiscountValue();
    }

    private DiscountType menuItemDiscountDiscountDiscountType(MenuItemDiscount menuItemDiscount) {
        Discount discount = menuItemDiscount.getDiscount();
        if ( discount == null ) {
            return null;
        }
        return discount.getDiscountType();
    }

    private LocalDate menuItemDiscountDiscountValidFrom(MenuItemDiscount menuItemDiscount) {
        Discount discount = menuItemDiscount.getDiscount();
        if ( discount == null ) {
            return null;
        }
        return discount.getValidFrom();
    }

    private LocalDate menuItemDiscountDiscountValidTo(MenuItemDiscount menuItemDiscount) {
        Discount discount = menuItemDiscount.getDiscount();
        if ( discount == null ) {
            return null;
        }
        return discount.getValidTo();
    }
}
