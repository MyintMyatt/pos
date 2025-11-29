package com.pos.features.super_admin.inventory.util;

import com.pos.constant.InventoryMovementType;
import com.pos.constant.Uom;
import com.pos.features.super_admin.inventory.model.entity.Inventory;
import com.pos.features.super_admin.inventory.model.entity.InventoryMovement;
import com.pos.features.super_admin.inventory.model.response.InventoryMovementResponse;
import com.pos.features.super_admin.inventory.model.response.InventoryResponse;
import com.pos.features.super_admin.inventory.model.response.InventorySimpleResponseWithMenu;
import com.pos.features.super_admin.menu_n_category.model.response.MenuSimpleResponse;
import com.pos.features.super_admin.menu_n_category.util.MenuMapper;
import com.pos.features.super_admin.user.model.response.UserSimpleResponse;
import com.pos.features.super_admin.user.util.UserMapper;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-29T13:52:04+0630",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class InventoryMapperImpl implements InventoryMapper {

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public InventoryResponse toFullResponse(Inventory inventory) {
        if ( inventory == null ) {
            return null;
        }

        String inventoryId = null;
        MenuSimpleResponse menuItem = null;
        double quantity = 0.0d;
        Uom uom = null;
        UserSimpleResponse createdBy = null;
        UserSimpleResponse updatedBy = null;

        inventoryId = inventory.getInventoryId();
        menuItem = menuMapper.toSimpleResponse( inventory.getMenuItem() );
        if ( inventory.getQuantity() != null ) {
            quantity = inventory.getQuantity();
        }
        uom = inventory.getUom();
        createdBy = userMapper.toSimpleResponse( inventory.getCreatedBy() );
        updatedBy = userMapper.toSimpleResponse( inventory.getUpdatedBy() );

        String createdDate = inventory.getCreatedDate().toString();
        String updatedDate = inventory.getUpdatedDate() != null ? inventory.getUpdatedDate().toString() : null;

        InventoryResponse inventoryResponse = new InventoryResponse( inventoryId, menuItem, quantity, uom, createdDate, createdBy, updatedDate, updatedBy );

        return inventoryResponse;
    }

    @Override
    public InventorySimpleResponseWithMenu toSimpleResponse(Inventory inventory) {
        if ( inventory == null ) {
            return null;
        }

        String inventoryId = null;
        MenuSimpleResponse menu = null;
        Integer quantity = null;
        Uom uom = null;

        inventoryId = inventory.getInventoryId();
        menu = menuMapper.toSimpleResponse( inventory.getMenuItem() );
        quantity = inventory.getQuantity();
        uom = inventory.getUom();

        InventorySimpleResponseWithMenu inventorySimpleResponseWithMenu = new InventorySimpleResponseWithMenu( inventoryId, menu, quantity, uom );

        return inventorySimpleResponseWithMenu;
    }

    @Override
    public InventoryMovementResponse toFullInvMovementResponse(InventoryMovement inventoryMovement) {
        if ( inventoryMovement == null ) {
            return null;
        }

        long inventoryMovementId = 0L;
        int beforeQty = 0;
        int afterQty = 0;
        int quantityChange = 0;
        InventoryMovementType inventoryMovementType = null;
        InventorySimpleResponseWithMenu currentInventoryStock = null;
        String createdDate = null;
        UserSimpleResponse createdBy = null;

        if ( inventoryMovement.getInventoryMovementId() != null ) {
            inventoryMovementId = inventoryMovement.getInventoryMovementId();
        }
        if ( inventoryMovement.getBeforeQty() != null ) {
            beforeQty = inventoryMovement.getBeforeQty();
        }
        if ( inventoryMovement.getAfterQty() != null ) {
            afterQty = inventoryMovement.getAfterQty();
        }
        if ( inventoryMovement.getQuantityChange() != null ) {
            quantityChange = inventoryMovement.getQuantityChange();
        }
        inventoryMovementType = inventoryMovement.getInventoryMovementType();
        currentInventoryStock = toSimpleResponse( inventoryMovement.getInventory() );
        if ( inventoryMovement.getCreatedDate() != null ) {
            createdDate = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( inventoryMovement.getCreatedDate() );
        }
        createdBy = userMapper.toSimpleResponse( inventoryMovement.getCreatedBy() );

        InventoryMovementResponse inventoryMovementResponse = new InventoryMovementResponse( inventoryMovementId, beforeQty, afterQty, quantityChange, inventoryMovementType, currentInventoryStock, createdDate, createdBy );

        return inventoryMovementResponse;
    }
}
