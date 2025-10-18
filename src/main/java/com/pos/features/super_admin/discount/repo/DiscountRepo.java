package com.pos.features.super_admin.discount.repo;

import com.pos.features.super_admin.discount.model.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepo extends JpaRepository<Discount, String> {
}
