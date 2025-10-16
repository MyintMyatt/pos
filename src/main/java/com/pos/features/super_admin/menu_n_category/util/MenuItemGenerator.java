package com.pos.features.super_admin.menu_n_category.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.time.LocalDate;

public class MenuItemGenerator implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session, Object o) {

        LocalDate now = LocalDate.now();
        String year = String.format("%02d", now.getYear() % 100);
        String month = String.format("%02d", now.getMonthValue());
        String sql = "SELECT menu_id FROM tbl_menu_item ORDER BY menu_id DESC LIMIT 1";
        String lastId = session.createNativeQuery(sql).uniqueResult().toString();

        int nextNumber = 1;
        if (lastId != null && lastId.length() >= 9) {
            String numberPart = lastId.substring(5);
            try {
                nextNumber = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                nextNumber = 1;
            }
        }
        return "M" + year + month + String.format("%05d", nextNumber);
    }

}
