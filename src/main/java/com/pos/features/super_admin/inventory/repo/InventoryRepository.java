package com.pos.features.super_admin.inventory.repo;

import com.pos.features.super_admin.inventory.model.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, String> {
    Inventory findByMenuItem_MenuId(String menuId);
}
