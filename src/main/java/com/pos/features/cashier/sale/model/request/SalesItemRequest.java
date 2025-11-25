package com.pos.features.cashier.sale.model.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesItemRequest {

    @NotBlank(message = "menu id must be required")
    private String menuId;

    @Min(value = 0, message = "quantity must not be negative")
    @NotNull(message = "quantity must not be null")
    private Integer quantity;

//    @DecimalMin(value = "0.0", inclusive = true, message = "price amount cannot be negative")
//    @NotNull(message = "price must not be null")
//    private Double price;

//    @DecimalMin(value = "0.0", inclusive = true, message = "Total amount cannot be negative")
//    @NotNull(message = "total amount must not be null")
//    private Double total;


}
