package com.pos.features.super_admin.discount.service;

import com.pos.exception.NotFoundException;
import com.pos.features.super_admin.discount.model.entity.Discount;
import com.pos.features.super_admin.discount.model.entity.MenuItemDiscount;
import com.pos.features.super_admin.discount.model.request.MenuItemDiscountRequest;
import com.pos.features.super_admin.discount.model.response.MenuItemDiscountResponse;
import com.pos.features.super_admin.discount.repo.MenuItemDiscountRepository;
import com.pos.features.super_admin.menu_n_category.model.entity.MenuItem;
import com.pos.features.super_admin.menu_n_category.repo.MenuRepo;
import com.pos.features.super_admin.user.model.entity.User;
import com.pos.features.super_admin.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuItemDiscountService {

    @Autowired
    private MenuItemDiscountRepository menuItemDiscountRepo;

    @Autowired
    private MenuRepo menuItemRepo;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private UserService userService;

    /**
     * Create a new MenuItemDiscount
     */
    @Transactional
    public MenuItemDiscountResponse createMenuItemDiscount(MenuItemDiscountRequest req) {
        MenuItem menu = menuItemRepo.findById(req.getMenuId())
                .orElseThrow(() -> new NotFoundException("Menu not found with id " + req.getMenuId()));

        // Check if discount already assigned
        menuItemDiscountRepo.findByMenuItem(menu).ifPresent(m -> {
            throw new RuntimeException("This menu already has a discount assigned");
        });

        Discount discount = discountService.getDiscount(req.getDiscountId());
        boolean valid = !discount.isDeleted()
                && LocalDate.now().isAfter(discount.getValidFrom().minusDays(1))
                && LocalDate.now().isBefore(discount.getValidTo().plusDays(1));

        if (!valid) {
            throw new RuntimeException("Discount is not valid within the date range");
        }

        User user = userService.getUser(req.getUserId());

        MenuItemDiscount entity = MenuItemDiscount.builder()
                .menuItem(menu)
                .discount(discount)
                .createdDate(LocalDate.now())
                .createdBy(user)
                .build();

        menuItemDiscountRepo.save(entity);

        // Update menu item status
        menu.setThereDiscount(true);
        menuItemRepo.save(menu);

        return convertToResponse(entity);
    }

    /**
     * Get all menu-item-discounts
     */
    @Transactional(readOnly = true)
    public List<MenuItemDiscountResponse> getAllMenuItemDiscounts() {
        return menuItemDiscountRepo.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get by ID
     */
    @Transactional(readOnly = true)
    public MenuItemDiscountResponse getMenuItemDiscount(Long id) {
        MenuItemDiscount entity = menuItemDiscountRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("MenuItemDiscount not found with id " + id));
        return convertToResponse(entity);
    }

    /**
     * Delete
     */
    @Transactional
    public void deleteMenuItemDiscount(Long id) {
        MenuItemDiscount entity = menuItemDiscountRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("MenuItemDiscount not found with id " + id));

        MenuItem menu = entity.getMenuItem();
        menu.setThereDiscount(false);
        menuItemRepo.save(menu);

        menuItemDiscountRepo.delete(entity);
    }

    /**
     * Auto job to deactivate expired discounts
     * Runs daily at 1:00 AM
     */
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void deactivateExpiredDiscounts() {
        List<MenuItemDiscount> discounts = menuItemDiscountRepo.findAll();
        LocalDate today = LocalDate.now();

        for (MenuItemDiscount mid : discounts) {
            Discount dis = mid.getDiscount();
            if (dis.isDeleted() || today.isAfter(dis.getValidTo())) {
                MenuItem menu = mid.getMenuItem();
                menu.setThereDiscount(false);
                menuItemRepo.save(menu);
            }
        }
    }

    private MenuItemDiscountResponse convertToResponse(MenuItemDiscount obj) {
        return MenuItemDiscountResponse.builder()
                .id(obj.getId())
                .menuId(obj.getMenuItem().getMenuId())
                .menuName(obj.getMenuItem().getMenuName())
                .discountId(obj.getDiscount().getDiscountId())
                .discountType(obj.getDiscount().getDiscountType().name())
                .discountValue(obj.getDiscount().getDiscountValue())
                .validFrom(obj.getDiscount().getValidFrom())
                .validTo(obj.getDiscount().getValidTo())
                .createdDate(obj.getCreatedDate())
                .createdBy(obj.getCreatedBy().getUserName())
                .build();
    }
}
