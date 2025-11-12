package com.pos.features.super_admin.menu_n_category.repo;

import com.pos.features.super_admin.menu_n_category.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category, String> {
}
