package com.pos.features.super_admin.tax.model.request;

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
public class TaxRequest {

    @NotNull(message = "tax description is required")
    private String taxDesc;

    @NotNull(message = "tax percentage value is required")
    @Min(value = 0, message = "tax percentage must be greater than zero")
    private Double taxPercentage;

    @NotNull(message = "tax valid from date is required")
    private LocalDate validFrom;

    @NotNull(message = "tax valid to date is required")
    private LocalDate validTo;

    @NotNull(message = "tax creator is required")
    private String userId;
}
