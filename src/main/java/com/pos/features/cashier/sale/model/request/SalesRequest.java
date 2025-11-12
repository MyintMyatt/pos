package com.pos.features.cashier.sale.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesRequest {

    @NotNull(message = "creator is required")
    private String userId;

    @NotNull(message = "Sale date is required")
    private LocalDate saleDate;

    @NotNull(message = "Items list cannot be null")
    @Size(min = 1, message = "At least one item is required")
    private List<SalesItemRequest> items;

    @NotNull(message = "tax id list cannot be null")
    @Size(min = 1, message = "At least one item is required")
    private List<String> taxIds;

}
