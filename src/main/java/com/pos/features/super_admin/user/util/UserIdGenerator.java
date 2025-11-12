package com.pos.features.super_admin.user.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import java.io.Serializable;
import java.time.LocalDate;

public class UserIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {

        LocalDate now = LocalDate.now();
        String year = String.format("%02d", now.getYear() % 100);
        String month = String.format("%02d", now.getMonthValue());
        String prefix = "UID" + year + month;

        String sql = "SELECT user_id FROM tbl_users ORDER BY user_id DESC LIMIT 1";
        String lastId = (String) session.createNativeQuery(sql).uniqueResult();

        int nextNumber = 1;

        if (lastId != null && lastId.length() >= 10) {
            String numberPart = lastId.substring(7);
            try {
                nextNumber = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                nextNumber = 1;
            }
        }

        String newId = prefix + String.format("%04d", nextNumber);
        return newId;
    }
}
