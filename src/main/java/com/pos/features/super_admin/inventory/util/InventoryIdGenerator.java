package com.pos.features.super_admin.inventory.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.time.LocalDate;

public class InventoryIdGenerator implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session, Object o) {

        LocalDate now = LocalDate.now();
        String year = String.format("%02d", now.getYear() % 100);
        String month = String.format("%02d", now.getMonthValue());
        String sql = "SELECT inventory_id FROM tbl_inventory ORDER BY inventory_id DESC LIMIT 1";
        String lastId = session.createNativeQuery(sql).uniqueResult().toString();

        int nextNum = 1;
        if (lastId != null && lastId.length() >= 8){
            String nextPart = lastId.substring(7);
            try {
                nextNum = Integer.parseInt(nextPart) + 1;
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
        }
        return "INV" + year + month + String.format("%05d", nextNum);
    }
}
