package com.pos.features.super_admin.discount.controller;

import com.pos.common.model.response.ApiResponse;
import com.pos.features.super_admin.discount.model.request.DiscountRequest;
import com.pos.features.super_admin.discount.service.DiscountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/super-admin/discount")
@Tag(name = "Super Admin Discount API", description = "Endpoints for managing discounts")
public class DiscountController {
    @Autowired
    private DiscountService discountService;

    @Operation(
            summary = "Discount Creation",
            description = "create new discount",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "discount request",
                    required = true,
                    content = @Content(schema = @Schema(implementation =  DiscountRequest.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201",description = "successfully created discount!!"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500",description = "internal server error")
            }
    )
    @PostMapping
    public ResponseEntity<?> createDis(@Valid @RequestBody DiscountRequest obj) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.builder()
                                .status(201)
                                .message("successfully created discount!!")
                                .data(discountService.createDiscount(obj))
                                .build()
                );
    }


    @Operation(
            summary = "Get all discount",
            description = "Retrieve all discount from system",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "all registered discount list"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500",description = "internal server error")
            }
    )
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllDiscounts() {
        return ResponseEntity.status(200).body(
                ApiResponse.builder()
                        .status(200)
                        .message("all registered discount list")
                        .data(discountService.getAllDiscount())
                        .build()
        );
    }


    @Operation(
            summary = "get discount by  id",
            description = "retrieve specific discount by id ",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "retrieve discount",
                    required = true,
                    content = @Content(schema = @Schema(implementation = String.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201",description = "discount that equals by id"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",description = "discount not found")
            }
    )
    @GetMapping("/{disId}")
    public ResponseEntity<ApiResponse<Object>> getDiscountById(@PathVariable("disId") String disId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.builder()
                        .status(200)
                        .message("discount that equals " + disId)
                        .data(discountService.getDiscount(disId))
                        .build()
        );
    }


    @Operation(
            summary = "Update discount",
            description = "update discount information",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "update discount",
                    required = true,
                    content = {
                            @Content(schema = @Schema(implementation = String.class)),
                            @Content(schema = @Schema(implementation = DiscountRequest.class)),
                    }
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201",description = "successfully registered discount!!"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500",description = "internal server error")
            }
    )
    @PutMapping("/{discount-id}")
    public ResponseEntity<ApiResponse<?>> updateDiscount(@PathVariable("discount-id") String disId, @RequestBody DiscountRequest obj) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.builder()
                        .status(200)
                        .message("successfully updated discount")
                        .data(discountService.updateDiscount(disId, obj))
                        .build()
        );
    }

    @Operation(
            summary = "Delete discount",
            description = "delete discount by id",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "require discount id",
                    required = true,
                    content = @Content(schema = @Schema(implementation = String.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "successfully deleted discount"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",description = "discount not found")
            }
    )
    @DeleteMapping("/{disId}")
    public ResponseEntity<ApiResponse<?>> deleteDiscount(@PathVariable("disId") String disId) {
        discountService.deleteDiscount(disId);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(200)
                        .message("successfully deleted discount")
                        .data(null)
                        .build()
        );
    }
}
