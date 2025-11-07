package com.pos.features.cashier.sale.service;

import com.pos.constant.DiscountType;
import com.pos.constant.InventoryMovementType;
import com.pos.constant.Uom;
import com.pos.features.cashier.sale.model.entity.SaleTaxId;
import com.pos.features.cashier.sale.model.entity.Sales;
import com.pos.features.cashier.sale.model.entity.SalesItem;
import com.pos.features.cashier.sale.model.entity.SalesTax;
import com.pos.features.cashier.sale.model.request.SalesItemRequest;
import com.pos.features.cashier.sale.model.request.SalesRequest;
import com.pos.features.cashier.sale.model.response.SalesItemResponse;
import com.pos.features.cashier.sale.model.response.SalesResponse;
import com.pos.features.cashier.sale.repo.SalesItemRepo;
import com.pos.features.cashier.sale.repo.SalesRepo;
import com.pos.features.cashier.sale.repo.SalesTaxRepo;
import com.pos.features.super_admin.discount.model.entity.Discount;
import com.pos.features.super_admin.discount.model.entity.MenuItemDiscount;
import com.pos.features.super_admin.discount.repo.MenuItemDiscountRepository;
import com.pos.features.super_admin.inventory.model.entity.Inventory;
import com.pos.features.super_admin.inventory.model.request.InventoryMovementRequest;
import com.pos.features.super_admin.inventory.service.InventoryService;
import com.pos.features.super_admin.menu_n_category.model.entity.MenuItem;
import com.pos.features.super_admin.menu_n_category.service.MenuService;
import com.pos.features.super_admin.tax.model.entity.Tax;
import com.pos.features.super_admin.tax.service.TaxService;
import com.pos.features.super_admin.user.model.entity.User;
import com.pos.features.super_admin.user.service.UserService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@NoArgsConstructor
public class SalesService {

    private SalesRepo salesRepo;
    private SalesItemRepo salesItemRepo;
    private SalesTaxRepo salesTaxRepo;
    private UserService userService;
    private MenuService menuService;
    private TaxService taxService;
    private MenuItemDiscountRepository menuItemDiscountRepository;
    private InventoryService inventoryService;


    @Autowired
    public SalesService(InventoryService inventoryService, SalesTaxRepo salesTaxRepo, SalesRepo salesRepo, @Lazy UserService userService,@Lazy MenuService menuService, TaxService taxService, MenuItemDiscountRepository menuItemDiscountRepository,SalesItemRepo salesItemRepo) {
        this.salesRepo = salesRepo;
        this.userService = userService;
        this.menuService = menuService;
        this.taxService = taxService;
        this.menuItemDiscountRepository = menuItemDiscountRepository;
        this.salesItemRepo = salesItemRepo;
        this.salesTaxRepo = salesTaxRepo;
        this.inventoryService = inventoryService;
    }


    @CacheEvict(value = "salesCache", allEntries = true)
    @Transactional
    public SalesResponse createSale(SalesRequest request) {
        //check user is exited or not
        User createdBy = userService.getUser(request.getUserId());

        double subtotal = 0.0;

//        //calculate subtotal
//        for (SalesItemRequest itemRequest : request.getItems()){
//            subtotal += itemRequest.getPrice() * itemRequest.getQuantity();
//        }

        //create sale
        Sales sales = Sales.builder()
                .saleDate(LocalDate.now())
                .subTotal(subtotal)
                .totalAmount(0.0)
                .createdBy(createdBy)
                .createdDate(LocalDate.now())
                .build();
        sales = salesRepo.save(sales);

        // save sale items
        List<SalesItem> salesItems = new ArrayList<>();
        for (SalesItemRequest itemRequest : request.getItems()) {
            //check menu is exited or not
            MenuItem menuItem = menuService.getMenuItemById(itemRequest.getMenuId());


            //for discount
            double originalPrice = itemRequest.getPrice();
            double finalPrice = originalPrice;
            double discountAmount = 0.0;
            // Apply Discount (if available)
            if (menuItem.isThereDiscount()) {
                List<MenuItemDiscount> itemDiscounts =
                        menuItemDiscountRepository.findByMenuItem_MenuId(menuItem.getMenuId());
                /*boolean valid = !discount.isDeleted()
                        && !LocalDate.now().isBefore(discount.getValidFrom())  // today >= validFrom
                        && !LocalDate.now().isAfter(discount.getValidTo());    // today <= validTo
                */
                for (MenuItemDiscount mid : itemDiscounts) {
                    Discount discount = mid.getDiscount();
                    boolean valid = !discount.isDeleted()
                            && LocalDate.now().isAfter(discount.getValidFrom().minusDays(1))
                            && LocalDate.now().isBefore(discount.getValidTo().plusDays(1));

                    if (valid) {
                        if (discount.getDiscountType() == DiscountType.percentage) {
                            discountAmount = originalPrice * (discount.getDiscountValue() / 100.0);
                        } else if (discount.getDiscountType() == DiscountType.amount) {
                            discountAmount = discount.getDiscountValue();
                        }
                        finalPrice = originalPrice - discountAmount;
                        break; // apply only first valid discount
                    }
                }
            }


            double itemTotal = itemRequest.getQuantity() * finalPrice;
            subtotal += itemTotal;

            SalesItem salesItem = SalesItem.builder()
                    .sales(sales)
                    .menuItem(menuItem)
                    .quantity(itemRequest.getQuantity())
                    .price(finalPrice)
                    .total(itemTotal)
                    .build();
            salesItems.add(salesItem);

            //for inventory
           Inventory inventory = inventoryService.getInventoryByMenuId(salesItem.getMenuItem().getMenuId());
           inventoryService.inventoryMovementTypeCheck(
                   InventoryMovementRequest.builder()
                           .menuId(salesItem.getMenuItem().getMenuId())
                           .movementType(InventoryMovementType.SALE)
                           .quantity(salesItem.getQuantity())
                           .uom(Uom.Qty)
                           .createdBy(createdBy.getUserId())
                           .build()
                   , inventory,createdBy,menuItem);

        }

        salesItemRepo.saveAll(salesItems);
        sales.setSalesItems(salesItems);

        // tax
        double totalTaxAmount = 0.0;
        List<SalesTax> salesTaxList = new ArrayList<>();

        if (request.getTaxIds() != null && request.getTaxIds().size() != 0) {
            for (String taxId : request.getTaxIds()) {
                // check tax is exited or not
                Tax tax = taxService.getTaxById(taxId);

                double taxAmount = subtotal * (tax.getTaxPercentage() / 100);
                totalTaxAmount += taxAmount;

                SalesTax salesTax = SalesTax.builder()
                        .saleTaxId(new SaleTaxId(sales.getSalesId(), tax.getTaxId()))
                        .sales(sales)
                        .tax(tax)
                        .taxAmount(taxAmount)
                        .build();
                salesTaxList.add(salesTax);
            }
            salesTaxRepo.saveAll(salesTaxList);
        }



        sales.setSubTotal(subtotal);
        sales.setTotalAmount(subtotal + totalTaxAmount);
        salesRepo.save(sales);

        return convertToSalesResponse(sales);
    }

    @Cacheable(value = "salesCache", key = "#page + '-' + #size + '-' + #keyword")
    @Transactional
    public Page<SalesResponse> getAllSales(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Sales> salesPage;
        if (keyword != null && !keyword.isEmpty()) {
            salesPage = salesRepo.searchSales(keyword, pageable); // <-- JPQL query
        } else {
            salesPage = salesRepo.findAll(pageable);
        }

        return salesPage.map(this::convertToSalesResponse);
    }

    private SalesResponse convertToSalesResponse(Sales sale) {
        List<SalesItemResponse> itemResponses = sale.getSalesItems() // you need @OneToMany in Sales
                .stream()
                .map(item -> SalesItemResponse.builder()
                        .saleItemId(item.getSaleItemId())
                        .menuId(item.getMenuItem().getMenuId())
                        .menuName(item.getMenuItem().getMenuName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .total(item.getTotal())
                        .build()
                ).toList();

        return SalesResponse.builder()
                .salesId(sale.getSalesId())
                .saleDate(sale.getSaleDate().toString())
                .subTotal(sale.getSubTotal())
                .totalAmount(sale.getTotalAmount())
                .createdBy(userService.convertUserToUserResponse(sale.getCreatedBy()))
                .createdDate(sale.getCreatedDate().toString())
                .updatedBy(sale.getUpdatedBy() != null ? userService.convertUserToUserResponse(sale.getUpdatedBy()): null)
                .updatedDate(sale.getUpdatedDate() != null ? sale.getUpdatedDate().toString() : null)
                .items(itemResponses)
                .build();
    }


}
