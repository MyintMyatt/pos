package com.pos.features.super_admin.menu_n_category.service;

import com.pos.common.model.response.PageDTO;
import com.pos.common.service.SecurityService;
import com.pos.constant.InventoryMovementType;
import com.pos.exception.NotFoundException;
import com.pos.features.super_admin.discount.repo.MenuItemDiscountRepository;
import com.pos.features.super_admin.inventory.model.entity.Inventory;
import com.pos.features.super_admin.inventory.model.entity.InventoryMovement;
import com.pos.features.super_admin.inventory.repo.InventoryMovementRepository;
import com.pos.features.super_admin.inventory.service.InventoryService;
import com.pos.features.super_admin.menu_n_category.model.entity.Category;
import com.pos.features.super_admin.menu_n_category.model.entity.MenuItem;
import com.pos.features.super_admin.menu_n_category.model.request.MenuRequest;
import com.pos.features.super_admin.menu_n_category.model.response.MenuResponse;
import com.pos.features.super_admin.menu_n_category.repo.MenuRepo;
import com.pos.features.super_admin.menu_n_category.util.MenuItemGenerator;
import com.pos.features.super_admin.menu_n_category.util.MenuMapper;
import com.pos.features.super_admin.user.model.entity.User;
import com.pos.features.super_admin.user.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MenuService {

    private MenuRepo menuRepo;
    private CategoryService categoryService;
    private UserService userService;
    private InventoryService inventoryService;
    private MenuItemDiscountRepository menuItemDiscountRepository;
    private MenuMapper menuMapper;
    private SecurityService securityService;
    private InventoryMovementRepository inventoryMovementRepository;
//    private MenuItemGenerator menuItemGenerator;

    @Autowired
    public MenuService( InventoryMovementRepository inventoryMovementRepository, SecurityService securityService,MenuRepo menuRepo, CategoryService categoryService, UserService userService, InventoryService inventoryService, MenuItemDiscountRepository menuItemDiscountRepository, MenuMapper menuMapper) {
        this.inventoryMovementRepository = inventoryMovementRepository;
        this.securityService = securityService;
        this.menuRepo = menuRepo;
        this.categoryService = categoryService;
        this.userService = userService;
        this.inventoryService = inventoryService;
        this.menuItemDiscountRepository = menuItemDiscountRepository;
        this.menuMapper = menuMapper;
//        this.menuItemGenerator = menuItemGenerator;
    }

    @CacheEvict(value = {"InvMovement","menuCache","salesCache"}, allEntries = true)
    @Transactional
    public MenuResponse createMenu(MenuRequest request) {
        // check category and user that are invalid or not
        Category category = categoryService.getCategoryObjById(request.getCategoryId());
        User currentUser = userService.getUser(securityService.getCurrentLoginUserId());

        /*
        * @generate menu id
        */
        String menuId = generateNextMenuId();


        MenuItem menuItem = menuMapper.toEntity(request);
        menuItem.setMenuId(menuId);
        menuItem.setCategory(category);
        menuItem.setCreatedBy(currentUser);
        menuItem.setUpdatedBy(null);
        menuItem.setUpdatedDate(null);

        /*
        * @ generate inventory id manually
        * @ set inventory's properties
        */
        String inventoryId = inventoryService.generateNextInvId();
        Inventory inventory = menuItem.getInventory();
        inventory.setInventoryId(inventoryId);
        inventory.setMenuItem(menuItem);
        inventory.setCreatedBy(currentUser);

        menuItem.setInventory(inventory);

        /*
        * @ persist menu and inventory (cascade)
        */
        MenuItem savedResult = menuRepo.save(menuItem);
        //        entityManager.flush(); // Force all pending INSERTS (menu and inventory) to run NOW
        inventoryService.saveInventoryMovement(inventoryId,currentUser);

        return  menuMapper.toFullResponse(savedResult);
    }

    @CacheEvict(value = {"InvMovement","menuCache","salesCache"}, allEntries = true)
    @Transactional
    public MenuResponse updateMenu(String menuId, MenuRequest request) {

        User currentUser = userService.getUser(securityService.getCurrentLoginUserId());
        /*
        * @ retrieve existing menu
        * */
        MenuItem existingMenu = getMenuItemById(menuId);
        existingMenu.setUpdatedBy(currentUser);
        existingMenu.setUpdatedDate(LocalDate.now());
        existingMenu.setMenuName(request.getMenuName().trim());
        existingMenu.setPrice(request.getPrice());
        existingMenu.setDescription(request.getDescription());

        /*
        *  @ for category
        * */
        if (!existingMenu.getCategory().getCategoryId().trim().equals(request.getCategoryId())) {
            Category newCategory = categoryService.getCategoryObjById(request.getCategoryId());
            existingMenu.setCategory(newCategory);
        }

        /*
        * @ for inventory
        * @ this function is for if user add new quantity,
        * */
        if (request.getQuantity() > 0) {
            Inventory existingInv = existingMenu.getInventory();
            // add stock
            int oldQty = existingInv.getQuantity();
            int newQty = oldQty + request.getQuantity();

            existingInv.setQuantity(newQty);
            existingInv.setUom(request.getUom());
            existingMenu.setUpdatedBy(currentUser);
            existingMenu.setUpdatedDate(LocalDate.now());

            existingMenu.setInventory(existingInv);

            /*
            * @ for inventory movement
            * */
            InventoryMovement inventoryMovement = InventoryMovement.builder()
                    .quantityChange(request.getQuantity())
                    .beforeQty(oldQty)
                    .afterQty(newQty)
                    .inventoryMovementType(InventoryMovementType.RESTOCK)
                    .inventory(existingInv)
                    .createdDate(LocalDateTime.now())
                    .createdBy(currentUser)
                    .build();

            /*
            * @ save inventory movement obj
            * */
            inventoryMovementRepository.save(inventoryMovement);
        }

        return menuMapper.toFullResponse(menuRepo.save(existingMenu));
    }

    @Transactional
    public MenuItem getMenuItemById(String menuId) {

        MenuItem menuItem = menuRepo.findById(menuId).orElseThrow(
                () -> new NotFoundException("menu not found with id : " + menuId)
        );
        if (menuItem.isDeleted()) throw new NotFoundException("menu not found with id :" + menuId);
        return menuItem;
    }

    @Transactional
    public MenuResponse getMenuResponseById(String menuId) {
        return menuMapper.toFullResponse(getMenuItemById(menuId));
    }

    @CacheEvict(value = {"InvMovement","menuCache","salesCache"}, allEntries = true)
    @Transactional
    public void deleteMenu(String menuId) {
        String currentLoginUserId = securityService.getCurrentLoginUserId();
        User currentUser = userService.getUser(currentLoginUserId);

        MenuItem menu = getMenuItemById(menuId);
        menu.setDeleted(true);
        menu.setUpdatedDate(LocalDate.now());
        menu.setUpdatedBy(currentUser);
        menuRepo.save(menu);
    }

    @CacheEvict(value = {"InvMovement","menuCache","salesCache"}, allEntries = true)
    @Transactional
    public MenuResponse updateMenuImage(String menuId, String imageUrl) {
        MenuItem menuItem = getMenuItemById(menuId);
        menuItem.setMenuImageUrl(imageUrl);
        return menuMapper.toFullResponse(menuRepo.save(menuItem));
    }

    @Cacheable(value = "menuCache", key = "#page + '-' + #size + '-' + (#keyword != null ? #keyword : '') + '-' + (#categoryId != null ? #categoryId : '')")
    @Transactional
    public PageDTO<MenuResponse> getAllMenu(int page, int size, String keyword, String categoryId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MenuItem> menuPage = menuRepo.searchMenu(
                (keyword != null && !keyword.isBlank()) ? keyword : null,
                (categoryId != null && !categoryId.isBlank()) ? categoryId : null,
                pageable
        );

        List<MenuResponse> content = menuPage.stream().map(menuMapper::toFullResponse).toList();
        return new PageDTO<>(content, page, size, menuPage.getTotalElements(), menuPage.getTotalPages());
    }

    @PersistenceContext
    private EntityManager entityManager;

    // Use REQUIRES_NEW to ensure a separate, clean transaction for the ID lookup
    // Use 'synchronized' to prevent race conditions during ID generation
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public synchronized String generateNextMenuId() {
        String lastId = null;

        try {
            // Native SQL query to select the maximum menu_id
            // Adjust the table and column names to match your schema exactly
            String sql = "SELECT menu_id FROM tbl_menu_item ORDER BY menu_id DESC LIMIT 1";
            Query query = entityManager.createNativeQuery(sql);

            // Get the single result from the query
            lastId = (String) query.getSingleResult();

        } catch (NoResultException e) {
            // Handle the case where the table is empty
            lastId = null;
        } catch (Exception e) {
            // Handle other potential database errors
            e.printStackTrace();
            throw new RuntimeException("Error generating next menu ID", e);
        }
        LocalDate now = LocalDate.now();
        String year = String.format("%02d", now.getYear() % 100);
        String month = String.format("%02d", now.getMonthValue());

        int nextNumber = 1;
        if (lastId != null && lastId.length() >= 9) {
            String numberPart = lastId.substring(5);
            try {
                nextNumber = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                nextNumber = 1;
            }
        }
        return "M" + year + month + String.format("%05d", nextNumber);

//        // Implement your specific ID increment logic here
//        if (lastId == null || lastId.isEmpty()) {
//            return "MID10001"; // Starting ID
//        }
//
//        // Example logic to increment the numeric part (adjust to your exact format)
//        try {
//            String prefix = lastId.substring(0, 3); // e.g., "MID"
//            int number = Integer.parseInt(lastId.substring(3));
//            number++;
//            return prefix + number;
//        } catch (NumberFormatException | IndexOutOfBoundsException e) {
//            throw new RuntimeException("Invalid last menu ID format found in database: " + lastId, e);
//        }
    }

}
