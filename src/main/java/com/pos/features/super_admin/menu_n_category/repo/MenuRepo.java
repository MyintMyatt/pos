package com.pos.features.super_admin.menu_n_category.repo;

import com.pos.features.super_admin.menu_n_category.model.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepo extends JpaRepository<MenuItem, String> {

}
