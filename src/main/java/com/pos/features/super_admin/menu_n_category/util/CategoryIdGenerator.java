package com.pos.features.super_admin.menu_n_category.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class CategoryIdGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object o) {

        String sql = "SELECT category_id FROM tbl_category ORDER_BY user_id DESC LIMIT 1";
        String lastId = session.createNativeQuery(sql).uniqueResult().toString();

        int nextNumber = 1;
        if (lastId != null && lastId.length() >= 5){
            String numberPart = lastId.substring(1);
            try {
                nextNumber = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                nextNumber = 1;
            }
        }
        return "C" + String.format("%03d", nextNumber);
    }
}
