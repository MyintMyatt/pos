package com.pos.features.super_admin.discount.repo;

import com.pos.features.super_admin.discount.model.entity.MenuItemDiscount;
import com.pos.features.super_admin.menu_n_category.model.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MenuItemDiscountRepository extends JpaRepository<MenuItemDiscount, Long> {
    Optional<MenuItemDiscount> findByMenuItem(MenuItem menuItem);
    List<MenuItemDiscount> findByMenuItem_MenuId(String menuId);
}
