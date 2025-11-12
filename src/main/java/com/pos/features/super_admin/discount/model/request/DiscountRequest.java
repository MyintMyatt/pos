package com.pos.features.super_admin.discount.model.request;

import com.pos.constant.DiscountType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRequest {

    @NotNull(message = "discount type is required")
    private DiscountType discountType;

    @NotNull(message = "discount value is required")
    @Min(value = 0, message = "discount value must be greater than zero")
    private Double discountValue;

    @NotNull(message = "discount valid from date is required")
    private LocalDate validFrom;

    @NotNull(message = "discount valid to date is required")
    private LocalDate validTo;

    @NotNull(message = "discount creator is required")
    private String userId;
}
