package com.pos.features.cashier.sale.repo;

import com.pos.features.cashier.sale.model.entity.SaleTaxId;
import com.pos.features.cashier.sale.model.entity.SalesTax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesTaxRepo extends JpaRepository<SalesTax, SaleTaxId> {
}
