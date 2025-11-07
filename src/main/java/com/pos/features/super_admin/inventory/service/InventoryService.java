package com.pos.features.super_admin.inventory.service;

import com.pos.constant.InventoryMovementType;
import com.pos.exception.NotFoundException;
import com.pos.exception.OutOfStockException;
import com.pos.features.super_admin.inventory.model.entity.Inventory;
import com.pos.features.super_admin.inventory.model.entity.InventoryMovement;
import com.pos.features.super_admin.inventory.model.request.InventoryMovementRequest;
import com.pos.features.super_admin.inventory.model.response.InventoryResponse;
import com.pos.features.super_admin.inventory.repo.InventoryMovementRepository;
import com.pos.features.super_admin.inventory.repo.InventoryRepository;
import com.pos.features.super_admin.menu_n_category.model.entity.MenuItem;
import com.pos.features.super_admin.menu_n_category.service.MenuService;
import com.pos.features.super_admin.user.model.entity.User;
import com.pos.features.super_admin.user.service.UserService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@NoArgsConstructor
public class InventoryService {

    private InventoryRepository inventoryRepository;
    private InventoryMovementRepository movementRepository;
    private UserService userService;
    private MenuService menuService;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository, InventoryMovementRepository movementRepository, UserService userService, @Lazy MenuService menuService) {
        this.inventoryRepository = inventoryRepository;
        this.movementRepository = movementRepository;
        this.userService = userService;
        this.menuService = menuService;
    }


    @Transactional
    public InventoryResponse inventoryMovementControl(InventoryMovementRequest req) {
        System.err.println("Inventory Movement => " + req);
        // check user is exited or not
        User createdBy = userService.getUser(req.getCreatedBy());
        // check menu is exited or not, i.e. if  menu is deleted, no inventory operation will not be proceeded.
        MenuItem menuItem = menuService.getMenuItemById(req.getMenuId());

        // check if menu id is exited or not in inventory table
        Inventory inventory = inventoryRepository.findByMenuItem_MenuId(req.getMenuId());

       return inventoryMovementTypeCheck(req, inventory, createdBy, menuItem);
    }

    @Transactional
    public Inventory getInventoryByMenuId(String menuId){
        return inventoryRepository.findByMenuItem_MenuId(menuId);
    }

    @Transactional
    public InventoryResponse inventoryMovementTypeCheck(InventoryMovementRequest req, Inventory inventory, User createdBy, MenuItem menuItem){
        /*
         * this if condition checks
         * when inventory movement type is SALE or DAMAGE,menu item's inventory must not be null, it must be exited in inventory table
         *
         * */
        if (req.getMovementType() != InventoryMovementType.RESTOCK && inventory == null)
            throw new NotFoundException("Inventory does not existed for this menu id " + req.getMenuId());

        switch (req.getMovementType()) {
            case SALE -> {
                return convertInventoryToRes(inventoryMovementControlForSale(inventory, req, createdBy));
            }
            case RESTOCK -> {
                return convertInventoryToRes(inventoryMovementControlForReStock(req, menuItem, createdBy));
            }
            case DAMAGE -> {
                return convertInventoryToRes(inventoryMovementControlForDamage(inventory, req, createdBy));
            }
        }
        return null;
    }

    @Transactional
    private Inventory inventoryMovementControlForSale(Inventory inventory, InventoryMovementRequest req, User createdBy) {
        if (inventory.getQuantity() == 0 || inventory.getQuantity() < req.getQuantity())
            throw new OutOfStockException("Menu item quantity is not enough for this transaction. Current quantity is " + inventory.getQuantity());
        //Log for inventory movement
        recordForInventoryMovement(inventory, req, createdBy);
        // update stock (quantity) for menu item in inventory table
        inventory.setQuantity(inventory.getQuantity() - req.getQuantity());
        inventory.setUpdatedBy(createdBy);
        inventory.setUpdatedDate(LocalDate.now());
        return inventoryRepository.save(inventory);
    }

    @Transactional
    private Inventory inventoryMovementControlForReStock(InventoryMovementRequest req,MenuItem menuItem, User createdBy) {
        Inventory inventory = new Inventory();
        inventory.setMenuItem(menuItem);
        inventory.setQuantity(req.getQuantity());
        inventory.setUom(req.getUom());
        inventory.setCreatedBy(createdBy);
        inventory.setCreatedDate(LocalDate.now());
        inventory = inventoryRepository.save(inventory);
        //Log for inventory movement
        recordForInventoryMovement(inventory, req, createdBy);
        return inventory;
    }

    @Transactional
    private Inventory inventoryMovementControlForDamage(Inventory inventory, InventoryMovementRequest req, User createdBy) {
        if (inventory.getQuantity() == 0 || inventory.getQuantity() < req.getQuantity())
            throw new OutOfStockException("Menu item quantity is not enough for this transaction. Current quantity is " + inventory.getQuantity());
        //Log for inventory movement
        recordForInventoryMovement(inventory, req, createdBy);
        // update stock (quantity) for menu item in inventory table
        inventory.setQuantity(inventory.getQuantity() - req.getQuantity());
        inventory.setUpdatedBy(createdBy);
        inventory.setUpdatedDate(LocalDate.now());
        return inventoryRepository.save(inventory);
    }

    @Transactional
    private void recordForInventoryMovement(Inventory inventory, InventoryMovementRequest req, User createdBy) {
        InventoryMovement movement = new InventoryMovement();
        movement.setInventory(inventory);
        movement.setQuantityChange(req.getQuantity());
        movement.setInventoryMovementType(req.getMovementType());
        movement.setCreatedBy(createdBy);
        movement.setCreatedDate(LocalDateTime.now());

        movementRepository.save(movement);
    }

    private InventoryResponse convertInventoryToRes(Inventory obj){
        return new InventoryResponse(
                obj.getInventoryId(),
                obj.getMenuItem(),
                obj.getQuantity(),
                obj.getUom(),
                obj.getCreatedDate(),
                obj.getCreatedBy(),
                obj.getUpdatedDate(),
                obj.getUpdatedBy()
        );
    }
}
