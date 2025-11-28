package com.pos.features.cashier.sale.controller;

import com.pos.common.model.response.ApiResponse;
import com.pos.features.cashier.sale.model.request.SalesRequest;
import com.pos.features.cashier.sale.model.response.SalesResponse;
import com.pos.features.cashier.sale.service.SalesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/cashier/sales")
@Tag(name = "Cashier Sale Api", description = "Endpoints for managing sale")
public class SaleController {

    private SalesService salesService;

    public SaleController(SalesService salesService) {
        this.salesService = salesService;
    }

    @Operation(
            summary = "Sale Creation",
            description = "Create new sale",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "sale obj request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SalesRequest.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "successfully created sale!!"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500",description = "internal server error")
            }
    )
    @PreAuthorize("hasRole('CREATE')")
    @PostMapping
    public ResponseEntity<?> createSale(@Valid @RequestBody SalesRequest obj){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.builder()
                                .status(201)
                                .message("successfully created sale!!")
                                .data(salesService.createSale(obj))
                                .build()
                );
    }

    @Operation(
            summary = "Get all sales",
            description = "Retrieve all sales with pagination and optional keyword search by sales ID or creator name",
            parameters = {
                    @Parameter(
                            name = "page",
                            description = "Page number (zero-based)",
                            required = false,
                            example = "0"
                    ),
                    @Parameter(
                            name = "size",
                            description = "Page size",
                            required = false,
                            example = "10"
                    ),
                    @Parameter(
                            name = "keyword",
                            description = "Optional search keyword to filter sales by sales ID or creator name",
                            required = false
                    )
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved sales list"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"
                    )
            }
    )
    @PreAuthorize("hasRole('READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllSales(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate

    ) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(200)
                        .message("All sales list")
                        .data(salesService.getAllSales(page, size, keyword, startDate, endDate))
                        .build()
        );
    }

}
