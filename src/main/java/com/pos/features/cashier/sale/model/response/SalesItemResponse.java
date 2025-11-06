package com.pos.features.cashier.sale.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesItemResponse {
    private Long saleItemId;
    private String menuId;
    private String menuName;
    private Integer quantity;
    private Double price;
    private Double total;
}
