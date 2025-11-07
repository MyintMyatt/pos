package com.pos.features.super_admin.menu_n_category.service;

import com.pos.common.model.response.PageDTO;
import com.pos.exception.NotFoundException;
import com.pos.features.super_admin.discount.model.entity.MenuItemDiscount;
import com.pos.features.super_admin.discount.model.response.MenuItemDiscountResponse;
import com.pos.features.super_admin.discount.repo.MenuItemDiscountRepository;
import com.pos.features.super_admin.inventory.model.request.InventoryMovementRequest;
import com.pos.features.super_admin.inventory.service.InventoryService;
import com.pos.features.super_admin.menu_n_category.model.entity.Category;
import com.pos.features.super_admin.menu_n_category.model.entity.MenuItem;
import com.pos.features.super_admin.menu_n_category.model.request.MenuCreateRequest;
import com.pos.features.super_admin.menu_n_category.model.request.MenuUpdateRequest;
import com.pos.features.super_admin.menu_n_category.model.response.MenuResponse;
import com.pos.features.super_admin.menu_n_category.repo.MenuRepo;
import com.pos.features.super_admin.user.model.entity.User;
import com.pos.features.super_admin.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Service
public class MenuService {

    private MenuRepo menuRepo;
    private CategoryService categoryService;
    private UserService userService;
    private InventoryService inventoryService;
    private MenuItemDiscountRepository menuItemDiscountRepository;

    @Autowired
    public MenuService(MenuRepo menuRepo, CategoryService categoryService, UserService userService, InventoryService inventoryService, MenuItemDiscountRepository menuItemDiscountRepository) {
        this.menuRepo = menuRepo;
        this.categoryService = categoryService;
        this.userService = userService;
        this.inventoryService = inventoryService;
        this.menuItemDiscountRepository = menuItemDiscountRepository;
    }

    @CacheEvict(value = "menuCache", allEntries = true)
    @Transactional
    public MenuResponse createMenu(MenuCreateRequest obj) {
        Category c = categoryService.getCategoryObjById(obj.getCategoryId());
        User u = userService.getUser(obj.getCreatedBy());
        MenuItem menuItem = menuRepo.save(convertCreateReqToMenu(obj, c, u));

        // add stock
        inventoryService.inventoryMovementTypeCheck(
                InventoryMovementRequest.builder()
                        .menuId(menuItem.getMenuId())
                        .movementType(obj.getMovementType())
                        .quantity(obj.getQuantity())
                        .uom(obj.getUom())
                        .createdBy(u.getUserName())
                        .build(), null, u, menuItem
        );

        MenuResponse menuResponse =  convertObjToRes(menuItem);
        return menuResponse;
    }

    @CacheEvict(value = "menuCache", allEntries = true)
    @Transactional
    public MenuResponse updateMenu(String menuId, MenuUpdateRequest obj) {
        User updatedBy = userService.getUser(obj.getCategoryId());
        MenuItem existingMenu = getMenuItemById(menuId);
        Category category;

        // if update category, we need to update this
        if (existingMenu.getCategory().getCategoryId() != obj.getCategoryId()) {
            category = categoryService.getCategoryObjById(obj.getCategoryId());
            existingMenu.setCategory(category);
        }
        return convertObjToRes(menuRepo.save(convertUpdateReqToMenu(obj, existingMenu, updatedBy)));
    }

    @Transactional
    public MenuItem getMenuItemById(String menuId) {

        MenuItem menuItem = menuRepo.findById(menuId).orElseThrow(
                () -> new NotFoundException("menu not found with id " + menuId)
        );
        if (menuItem.isDeleted()) throw new NotFoundException("menu not found with id " + menuId);
        return menuItem;
    }

    @Transactional
    public MenuResponse getMenuResponseById(String menuId) {
        return convertObjToRes(getMenuItemById(menuId));
    }

    @CacheEvict(value = "menuCache", allEntries = true)
    @Transactional
    public void deleteMenu(String menuId) {
        MenuItem menu = getMenuItemById(menuId);
        menuRepo.save(menu);
    }

    @CacheEvict(value = "menuCache", allEntries = true)
    @Transactional
    public MenuResponse updateMenuImage(String menuId, String imageUrl) {
        MenuItem menuItem = getMenuItemById(menuId);
        menuItem.setMenuImageUrl(imageUrl);
        return convertObjToRes(menuRepo.save(menuItem));
    }

    //    @Transactional
//    public List<MenuResponse> getAllMenu() {
//        return menuRepo.findAll()
//                .stream().filter(m -> !m.isDeleted())
//                .map(this::convertObjToRes)
//                .toList();
//    }
    @Cacheable(value = "menuCache", key = "#page + '-' + #size + '-' + (#keyword != null ? #keyword : '') + '-' + (#categoryId != null ? #categoryId : '')")
    @Transactional
    public PageDTO<MenuResponse> getAllMenu(int page, int size, String keyword, String categoryId) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<MenuItem> menuPage = menuRepo.searchMenu(
//                (keyword != null && !keyword.isBlank()) ? keyword : null,
//                (categoryId != null && !categoryId.isBlank()) ? categoryId : null,
//                pageable
//        );
//        System.err.println("menu page => " + menuPage);
//
//        return menuPage.map(this::convertObjToRes);
        Pageable pageable = PageRequest.of(page, size);
        Page<MenuItem> menuPage = menuRepo.searchMenu(
                (keyword != null && !keyword.isBlank()) ? keyword : null,
                (categoryId != null && !categoryId.isBlank()) ? categoryId : null,
                pageable
        );

        List<MenuResponse> content = menuPage.stream().map(this::convertObjToRes).toList();
        return new PageDTO<>(content, page, size, menuPage.getTotalElements(), menuPage.getTotalPages());
    }


    private MenuItem convertCreateReqToMenu(MenuCreateRequest obj, Category category, User user) {
        return MenuItem.builder()
                .menuName(obj.getMenuName())
                .price(obj.getPrice())
                .category(category)
                .isThereDiscount(false)
                .description(obj.getDescription())
                .createdDate(LocalDate.now())
                .createdBy(user)
                .isDeleted(false)
                .build();
    }

    private MenuItem convertUpdateReqToMenu(MenuUpdateRequest obj, MenuItem existing, User updatedBy) {
        return MenuItem.builder()
                .menuName(obj.getMenuName())
                .price(obj.getPrice())
                .category(existing.getCategory())
                .isThereDiscount(false)
                .description(obj.getDescription())
                .createdDate(existing.getCreatedDate())
                .createdBy(existing.getCreatedBy())
                .updatedBy(updatedBy)
                .updatedDate(LocalDate.now())
                .isDeleted(false)
                .build();
    }

    public MenuResponse convertObjToRes(MenuItem obj) {
        return new MenuResponse(
                obj.getMenuId(),
                obj.getMenuName(),
                obj.getPrice(),
                categoryService.convertCategoryToRes(obj.getCategory()),
                obj.getMenuImageUrl(),
                obj.isThereDiscount(),
                obj.getDescription(),
                userService.convertUserToUserResponse(obj.getCreatedBy()),
                obj.getCreatedDate().toString(),
                obj.getUpdatedBy() != null ? userService.convertUserToUserResponse(obj.getUpdatedBy()) : null,
                obj.getUpdatedDate() != null ? obj.getUpdatedDate().toString() : null,
                inventoryService.convertObjToInvCustomRes(obj.getInventory()),
                getDiscountsForMenu(obj)
        );
    }

    @Transactional
    private List<MenuItemDiscountResponse> getDiscountsForMenu(MenuItem menuItem) {
        List<MenuItemDiscount> discounts = menuItemDiscountRepository.findByMenuItem_MenuId(menuItem.getMenuId());

        return discounts.stream()
                .filter(mid -> {
                    // Only valid discounts
                    var dis = mid.getDiscount();
                    return !dis.isDeleted()
                            && LocalDate.now().isAfter(dis.getValidFrom().minusDays(1))
                            && LocalDate.now().isBefore(dis.getValidTo().plusDays(1));
                })
                .map(mid -> MenuItemDiscountResponse.builder()
                        .id(mid.getId())
                        .menuId(menuItem.getMenuId())
                        .menuName(menuItem.getMenuName())
                        .discountId(mid.getDiscount().getDiscountId())
                        .discountValue(mid.getDiscount().getDiscountValue())
                        .discountType(mid.getDiscount().getDiscountType().name())
                        .validFrom(mid.getDiscount().getValidFrom())
                        .validTo(mid.getDiscount().getValidTo())
                        .createdDate(mid.getCreatedDate())
                        .createdBy(mid.getCreatedBy().getUserName()) // or getUsername()
                        .build())
                .toList();
    }

}
