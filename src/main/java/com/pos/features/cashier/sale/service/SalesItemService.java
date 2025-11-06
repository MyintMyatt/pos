package com.pos.features.cashier.sale.service;

import com.pos.features.cashier.sale.repo.SalesItemRepo;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class SalesItemService {

    private SalesItemRepo salesItemRepo;

    public SalesItemService(SalesItemRepo salesItemRepo) {
        this.salesItemRepo = salesItemRepo;
    }

}
