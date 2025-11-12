package com.pos.features.cashier.sale.repo;

import com.pos.features.cashier.sale.model.entity.SalesItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesItemRepo extends JpaRepository<SalesItem, Long> {
}
