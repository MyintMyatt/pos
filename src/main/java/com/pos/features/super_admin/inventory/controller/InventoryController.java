package com.pos.features.super_admin.inventory.controller;

import com.pos.common.model.response.ApiResponse;
import com.pos.features.super_admin.inventory.model.request.InventoryMovementRequest;
import com.pos.features.super_admin.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/inventory")
@Tag(name = "Admin Inventory Api", description = "Endpoints for managing menu item inventory ( sale, restock, damage) ")
public class InventoryController {

    private InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }


    @Operation(
            summary = "inventory control",
            description = "inventory movement control for sale, restock, and damage",
            requestBody =
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "inventory movement reqeust",
                    required = true,
                    content = @Content(schema = @Schema(implementation = InventoryMovementRequest.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "success"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "internal server error")
            }
    )
    @PostMapping
    public ResponseEntity<?> controlInventoryMovement(@Valid @RequestBody InventoryMovementRequest request) {
        return ResponseEntity
                .status(200)
                .body(
                        ApiResponse.builder()
                                .status(200)
                                .message("successfully !!")
                                .data(inventoryService.inventoryMovementControl(request))
                                .build()
                );
    }

    @GetMapping
    public ResponseEntity<?> getAllInventroyMovement(){
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(200)
                        .message("all inventory movement")
                        .data(inventoryService.getAllInvMovement()).build()
        );
    }


}
