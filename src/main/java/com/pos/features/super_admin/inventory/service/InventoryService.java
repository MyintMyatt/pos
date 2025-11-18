package com.pos.features.super_admin.inventory.service;

import com.pos.common.service.SecurityService;
import com.pos.constant.InventoryMovementType;
import com.pos.exception.NotFoundException;
import com.pos.exception.OutOfStockException;
import com.pos.features.super_admin.inventory.model.entity.Inventory;
import com.pos.features.super_admin.inventory.model.entity.InventoryMovement;
import com.pos.features.super_admin.inventory.model.request.InventoryMovementRequest;
import com.pos.features.super_admin.inventory.model.response.InventoryMovementResponse;
import com.pos.features.super_admin.inventory.model.response.InventoryResponse;
import com.pos.features.super_admin.inventory.repo.InventoryMovementRepository;
import com.pos.features.super_admin.inventory.repo.InventoryRepository;
import com.pos.features.super_admin.inventory.util.InventoryMapper;
import com.pos.features.super_admin.menu_n_category.model.entity.MenuItem;
import com.pos.features.super_admin.menu_n_category.service.MenuService;
import com.pos.features.super_admin.user.model.entity.User;
import com.pos.features.super_admin.user.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class InventoryService {

    private InventoryRepository inventoryRepository;
    private InventoryMovementRepository movementRepository;
    private UserService userService;
    private MenuService menuService;
    private SecurityService securityService;
    private InventoryMapper inventoryMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public InventoryService(InventoryMapper inventoryMapper, SecurityService securityService, InventoryRepository inventoryRepository, InventoryMovementRepository movementRepository, UserService userService, @Lazy MenuService menuService) {
        this.securityService = securityService;
        this.inventoryRepository = inventoryRepository;
        this.movementRepository = movementRepository;
        this.userService = userService;
        this.menuService = menuService;
        this.inventoryMapper = inventoryMapper;
    }

    @CacheEvict(value = "InvMovement",  allEntries = true)
    @Transactional
    public InventoryResponse inventoryMovementControl(InventoryMovementRequest req) {

        /*
        * @ get current login user
        * */
        String currentLoginUserId = securityService.getCurrentLoginUserId();
        User currentUser = userService.getUser(currentLoginUserId);

        /*
        * @ get menu obj
        * */
        MenuItem menuItem = menuService.getMenuItemById(req.getMenuId());

        // check if menu id is exited or not in inventory table
        Inventory inventory = inventoryRepository.findByMenuItem_MenuId(req.getMenuId());

       return inventoryMovementTypeCheck(req, inventory, currentUser, menuItem);
    }

    @Cacheable(value = "InvMovement")
    @Transactional
    public List<InventoryMovementResponse> getAllInvMovement(){
        return movementRepository.findAll()
                .stream()
                .map(inventoryMapper::toFullInvMovementResponse)
                .collect(Collectors.toList());
    }


    @CacheEvict(value = "InvMovement",  allEntries = true)
    @Transactional
    public Inventory getInventoryByMenuId(String menuId){
        return inventoryRepository.findByMenuItem_MenuId(menuId);
    }

    @CacheEvict(value = "InvMovement",  allEntries = true)
    @Transactional
    public InventoryResponse inventoryMovementTypeCheck(InventoryMovementRequest req, Inventory inventory, User currentUser, MenuItem menuItem){
        /*
         * this if condition checks
         * when inventory movement type is SALE or DAMAGE,menu item's inventory must not be null, it must be exited in inventory table
         *
         * */
        if (req.getMovementType() != InventoryMovementType.RESTOCK && inventory == null)
            throw new NotFoundException("Inventory does not existed for this menu id :" + req.getMenuId());

        switch (req.getMovementType()) {
            case SALE -> {
                return inventoryMapper.toFullResponse(inventoryMovementControlForSale(inventory, req, currentUser));
            }
            case RESTOCK -> {
                return inventoryMapper.toFullResponse(inventoryMovementControlForReStock(inventory, req, menuItem, currentUser));
            }
            case DAMAGE -> {
                return inventoryMapper.toFullResponse(inventoryMovementControlForDamage(inventory, req, currentUser));
            }
        }
        return null;
    }

    @Transactional
    private Inventory inventoryMovementControlForSale(Inventory inventory, InventoryMovementRequest req, User currentUser) {
        if (inventory.getQuantity() == 0 || inventory.getQuantity() < req.getQuantity())
            throw new OutOfStockException("Out of stock. Current quantity is " + inventory.getQuantity());
        /*
        * @ Log for inventory movement
        * */
        recordForInventoryMovement(inventory, req, currentUser);
        /*
        * @ update stock (quantity) for menu item in inventory table
        */
        inventory.setQuantity(inventory.getQuantity() - req.getQuantity());
        inventory.setUpdatedBy(currentUser);
        inventory.setUpdatedDate(LocalDate.now());
        return inventoryRepository.save(inventory);
    }

    @Transactional
    private Inventory inventoryMovementControlForReStock(Inventory exitingInv, InventoryMovementRequest req, MenuItem menuItem, User currentUser) {
        int currentStock = 0;
        Inventory inventory = null;
        if (exitingInv == null) {
            inventory = new Inventory();
            inventory.setInventoryId(generateNextInvId());
        }else {
            currentStock = exitingInv.getQuantity();
            inventory = exitingInv;
            inventory.setInventoryId(exitingInv.getInventoryId());
            inventory.setUpdatedDate(LocalDate.now());
            inventory.setUpdatedBy(currentUser);
        }
        inventory.setMenuItem(menuItem);
        inventory.setQuantity(currentStock + req.getQuantity());
        inventory.setUom(req.getUom());
        inventory.setCreatedBy(currentUser);
        inventory.setCreatedDate(LocalDate.now());
        inventory = inventoryRepository.save(inventory);
        /*
         * @ Log for inventory movement
         * */
        recordForInventoryMovement(inventory, req, currentUser);
        return inventory;
    }

    @Transactional
    private Inventory inventoryMovementControlForDamage(Inventory inventory, InventoryMovementRequest req, User currentUser) {
        if (inventory.getQuantity() == 0 || inventory.getQuantity() < req.getQuantity())
            throw new OutOfStockException("Out of stock. Current quantity is " + inventory.getQuantity());
        /*
         * @ Log for inventory movement
         * */
        recordForInventoryMovement(inventory, req, currentUser);
        // update stock (quantity) for menu item in inventory table
        inventory.setQuantity(inventory.getQuantity() - req.getQuantity());
        inventory.setUpdatedBy(currentUser);
        inventory.setUpdatedDate(LocalDate.now());
        return inventoryRepository.save(inventory);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void recordForInventoryMovement(Inventory inventory, InventoryMovementRequest req, User currentUser) {
        InventoryMovement movement = new InventoryMovement();
        movement.setInventory(inventory);
        movement.setQuantityChange(req.getQuantity());
        movement.setInventoryMovementType(req.getMovementType());
        movement.setCreatedBy(currentUser);
        movement.setCreatedDate(LocalDateTime.now());

        movementRepository.save(movement);
    }

    @Transactional
    public Inventory findInvById(String inventoryId) {
        return inventoryRepository.findById(inventoryId).orElseThrow(() ->
                new NotFoundException("inventory not found with id : " + inventoryId));
    }

    @Transactional
    public void saveInventoryMovement(String inventoryId, User currentUser) {
        System.err.println("Inv Id => " + inventoryId);
        Inventory inventory = findInvById(inventoryId);

        InventoryMovement inventoryMovement = InventoryMovement.builder()
                .inventory(inventory)
                .quantityChange(inventory.getQuantity())
                .inventoryMovementType(InventoryMovementType.RESTOCK)
                .createdDate(LocalDateTime.now())
                .createdBy(currentUser)
                .build();
        System.err.println("Saving inventory movement in NEW transaction.");
        movementRepository.save(inventoryMovement);
        System.err.println("Inventory movement saved successfully in NEW transaction.");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public synchronized String generateNextInvId(){
        String lastId = null;
        try {
            String sql = "SELECT inventory_id FROM tbl_inventory ORDER BY inventory_id DESC LIMIT 1";
            Query query = entityManager.createNativeQuery(sql);

            lastId = (String) query.getSingleResult();
        } catch (NoResultException e) {
            lastId = null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating next inventory id " + e);
        }

        LocalDate now = LocalDate.now();
        String year = String.format("%02d", now.getYear() % 100);
        String month = String.format("%02d", now.getMonthValue());

        int nextNum = 1;
        if (lastId != null && lastId.length() >= 8){
            String nextPart = lastId.substring(7);
            try {
                nextNum = Integer.parseInt(nextPart) + 1;
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
        }
        return "INV" + year + month + String.format("%05d", nextNum);
    }

}
