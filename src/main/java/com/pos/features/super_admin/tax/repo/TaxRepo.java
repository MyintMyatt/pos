package com.pos.features.super_admin.tax.repo;

import com.pos.features.super_admin.tax.model.entity.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxRepo extends JpaRepository<Tax, String> {
}
