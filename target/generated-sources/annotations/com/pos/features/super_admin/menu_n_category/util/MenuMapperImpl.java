package com.pos.features.super_admin.menu_n_category.util;

import com.pos.features.super_admin.discount.model.entity.MenuItemDiscount;
import com.pos.features.super_admin.discount.model.response.MenuItemDiscountResponse;
import com.pos.features.super_admin.inventory.model.response.InventorySimpleResponse;
import com.pos.features.super_admin.menu_n_category.model.entity.MenuItem;
import com.pos.features.super_admin.menu_n_category.model.request.MenuRequest;
import com.pos.features.super_admin.menu_n_category.model.response.CategorySimpleResponse;
import com.pos.features.super_admin.menu_n_category.model.response.MenuResponse;
import com.pos.features.super_admin.menu_n_category.model.response.MenuSimpleResponse;
import com.pos.features.super_admin.user.model.entity.User;
import com.pos.features.super_admin.user.model.response.UserSimpleResponse;
import com.pos.features.super_admin.user.util.UserMapper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-26T10:53:42+0630",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class MenuMapperImpl implements MenuMapper {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public MenuResponse toFullResponse(MenuItem menuItem) {
        if ( menuItem == null ) {
            return null;
        }

        String menuId = null;
        String menuName = null;
        double price = 0.0d;
        CategorySimpleResponse category = null;
        String imageUrl = null;
        boolean isThereDiscount = false;
        String description = null;
        UserSimpleResponse createdBy = null;
        String createdDate = null;
        UserSimpleResponse updatedBy = null;
        String updatedDate = null;
        List<MenuItemDiscountResponse> discounts = null;

        menuId = menuItem.getMenuId();
        menuName = menuItem.getMenuName();
        if ( menuItem.getPrice() != null ) {
            price = menuItem.getPrice();
        }
        category = categoryMapper.toSimpleResponse( menuItem.getCategory() );
        imageUrl = menuItem.getMenuImageUrl();
        isThereDiscount = menuItem.isThereDiscount();
        description = menuItem.getDescription();
        createdBy = userMapper.toSimpleResponse( menuItem.getCreatedBy() );
        if ( menuItem.getCreatedDate() != null ) {
            createdDate = DateTimeFormatter.ISO_LOCAL_DATE.format( menuItem.getCreatedDate() );
        }
        updatedBy = userMapper.toSimpleResponse( menuItem.getUpdatedBy() );
        if ( menuItem.getUpdatedDate() != null ) {
            updatedDate = DateTimeFormatter.ISO_LOCAL_DATE.format( menuItem.getUpdatedDate() );
        }
        discounts = menuItemDiscountListToMenuItemDiscountResponseList( menuItem.getMenuItemDiscounts() );

        InventorySimpleResponse inventory = toInvSimpleResponse(menuItem.getInventory());

        MenuResponse menuResponse = new MenuResponse( menuId, menuName, price, category, imageUrl, isThereDiscount, description, createdBy, createdDate, updatedBy, updatedDate, inventory, discounts );

        return menuResponse;
    }

    @Override
    public MenuSimpleResponse toSimpleResponse(MenuItem menuItem) {
        if ( menuItem == null ) {
            return null;
        }

        String menuId = null;
        String menuName = null;
        CategorySimpleResponse category = null;

        menuId = menuItem.getMenuId();
        menuName = menuItem.getMenuName();
        category = categoryMapper.toSimpleResponse( menuItem.getCategory() );

        MenuSimpleResponse menuSimpleResponse = new MenuSimpleResponse( menuId, menuName, category );

        return menuSimpleResponse;
    }

    @Override
    public MenuItem toEntity(MenuRequest request) {
        if ( request == null ) {
            return null;
        }

        MenuItem.MenuItemBuilder menuItem = MenuItem.builder();

        menuItem.menuName( request.getMenuName() );
        menuItem.price( request.getPrice() );
        menuItem.description( request.getDescription() );

        menuItem.inventory( toInventory(request) );
        menuItem.isThereDiscount( false );
        menuItem.createdDate( LocalDate.now() );

        return menuItem.build();
    }

    protected UserSimpleResponse userToUserSimpleResponse(User user) {
        if ( user == null ) {
            return null;
        }

        String userId = null;
        String userName = null;

        userId = user.getUserId();
        userName = user.getUserName();

        UserSimpleResponse userSimpleResponse = new UserSimpleResponse( userId, userName );

        return userSimpleResponse;
    }

    protected MenuItemDiscountResponse menuItemDiscountToMenuItemDiscountResponse(MenuItemDiscount menuItemDiscount) {
        if ( menuItemDiscount == null ) {
            return null;
        }

        MenuItemDiscountResponse.MenuItemDiscountResponseBuilder menuItemDiscountResponse = MenuItemDiscountResponse.builder();

        menuItemDiscountResponse.createdDate( menuItemDiscount.getCreatedDate() );
        menuItemDiscountResponse.createdBy( userToUserSimpleResponse( menuItemDiscount.getCreatedBy() ) );

        return menuItemDiscountResponse.build();
    }

    protected List<MenuItemDiscountResponse> menuItemDiscountListToMenuItemDiscountResponseList(List<MenuItemDiscount> list) {
        if ( list == null ) {
            return null;
        }

        List<MenuItemDiscountResponse> list1 = new ArrayList<MenuItemDiscountResponse>( list.size() );
        for ( MenuItemDiscount menuItemDiscount : list ) {
            list1.add( menuItemDiscountToMenuItemDiscountResponse( menuItemDiscount ) );
        }

        return list1;
    }
}
