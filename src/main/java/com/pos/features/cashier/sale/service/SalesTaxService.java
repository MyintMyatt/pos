package com.pos.features.cashier.sale.service;

import com.pos.features.cashier.sale.repo.SalesTaxRepo;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class SalesTaxService {

    private SalesTaxRepo salesTaxRepo;

    public SalesTaxService(SalesTaxRepo salesTaxRepo) {
        this.salesTaxRepo = salesTaxRepo;
    }
}
