package com.pos.features.super_admin.discount.controller;

import com.pos.common.model.response.ApiResponse;
import com.pos.features.super_admin.discount.model.request.MenuItemDiscountRequest;
import com.pos.features.super_admin.discount.service.MenuItemDiscountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/menu-discount")
@Tag(name = "Super Admin Menu Discount Setup API", description = "Endpoints for managing menu item discounts")
public class MenuItemDiscountController {

    @Autowired
    private MenuItemDiscountService service;

    @Operation(summary = "Create MenuItemDiscount")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody MenuItemDiscountRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder()
                        .status(201)
                        .message("Menu discount created successfully!")
                        .data(service.createMenuItemDiscount(req))
                        .build());
    }

    @Operation(summary = "Get All MenuItemDiscounts")
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(200)
                        .message("All menu item discounts")
                        .data(service.getAllMenuItemDiscounts())
                        .build()
        );
    }

    @Operation(summary = "Get MenuItemDiscount by ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(200)
                        .message("Menu discount found")
                        .data(service.getMenuItemDiscount(id))
                        .build()
        );
    }

    @Operation(summary = "Delete MenuItemDiscount")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.deleteMenuItemDiscount(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(200)
                        .message("Menu discount deleted successfully")
                        .build()
        );
    }
}
