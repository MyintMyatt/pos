package com.pos.features.super_admin.discount.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.time.LocalDate;

public class DiscountIdGenerator implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session, Object o) {
        LocalDate now = LocalDate.now();
        String year = String.format("%02d", now.getYear() % 100);
        String month = String.format("%02d", now.getMonthValue());
        String prefix = "DIS" + year + month;

        String sql = "SELECT discount_id FROM tbl_discount ORDER BY discount_id DESC LIMIT 1;";
        String lastId = (String) session.createNativeQuery(sql).uniqueResult();

        int nextId = 1;

        if (lastId != null && lastId.length() >= 7){
            String numberPart = lastId.substring(7);
            try {
                nextId = Integer.parseInt(numberPart);
            } catch (NumberFormatException e) {
                nextId = 1;
            }
        }
        return prefix + String.format("%04d", nextId);
    }
}
