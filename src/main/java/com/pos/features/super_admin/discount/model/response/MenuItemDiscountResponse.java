package com.pos.features.super_admin.discount.model.response;

import com.pos.features.super_admin.user.model.response.UserResponse;
import com.pos.features.super_admin.user.model.response.UserSimpleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemDiscountResponse implements Serializable {
//    private Long id;
//    private String menuId;
//    private String menuName;
    private String discountId;
    private Double discountValue;
    private String discountType;
    private LocalDate validFrom;
    private LocalDate validTo;
    private LocalDate createdDate;
    private UserSimpleResponse createdBy;
}
