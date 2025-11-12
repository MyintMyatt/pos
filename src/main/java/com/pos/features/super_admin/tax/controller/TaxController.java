package com.pos.features.super_admin.tax.controller;

import com.pos.common.model.response.ApiResponse;
import com.pos.features.super_admin.tax.model.request.TaxRequest;
import com.pos.features.super_admin.tax.service.TaxService;
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
@RequestMapping("/admin/tax")
@Tag(name = "Super Admin Tax API", description = "Endpoints for managing tax")
public class TaxController {
    @Autowired
    private TaxService taxService;

    @Operation(
            summary = "tax Creation",
            description = "create new tax",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "tax request",
                    required = true,
                    content = @Content(schema = @Schema(implementation =  TaxRequest.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201",description = "successfully created tax!!"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500",description = "internal server error")
            }
    )
    @PostMapping
    public ResponseEntity<?> createDis(@Valid @RequestBody TaxRequest obj) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.builder()
                                .status(201)
                                .message("successfully created tax!!")
                                .data(taxService.createTax(obj))
                                .build()
                );
    }


    @Operation(
            summary = "Get all tax",
            description = "Retrieve all tax from system",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "all registered tax list"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500",description = "internal server error")
            }
    )
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAlltaxs() {
        return ResponseEntity.status(200).body(
                ApiResponse.builder()
                        .status(200)
                        .message("all registered tax list")
                        .data(taxService.getAllTax())
                        .build()
        );
    }


//    @Operation(
//            summary = "get tax by  id",
//            description = "retrieve specific tax by id ",
//            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                    description = "retrieve tax",
//                    required = true,
//                    content = @Content(schema = @Schema(implementation = String.class))
//            ),
//            responses = {
//                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "tax that equals by id"),
//                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",description = "tax not found")
//            }
//    )
//    @GetMapping("/{disId}")
//    public ResponseEntity<ApiResponse<Object>> gettaxById(@PathVariable("disId") String disId) {
//        return ResponseEntity.status(HttpStatus.OK).body(
//                ApiResponse.builder()
//                        .status(200)
//                        .message("tax that equals " + disId)
//                        .data(taxService.gettax(disId))
//                        .build()
//        );
//    }


//    @Operation(
//            summary = "Update tax",
//            description = "update tax information",
//            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                    description = "update tax",
//                    required = true,
//                    content = {
//                            @Content(schema = @Schema(implementation = String.class)),
//                            @Content(schema = @Schema(implementation = TaxRequest.class)),
//                    }
//            ),
//            responses = {
//                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "successfully updated tax!!"),
//                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500",description = "internal server error")
//            }
//    )
//
//    @PutMapping("/{tax-id}")
//    public ResponseEntity<ApiResponse<?>> updatetax(@PathVariable("tax-id") String disId, @RequestBody TaxRequest obj) {
//        return ResponseEntity.status(HttpStatus.OK).body(
//                ApiResponse.builder()
//                        .status(200)
//                        .message("successfully updated tax")
//                        .data(taxService.updatetax(disId, obj))
//                        .build()
//        );
//    }
//
//    @Operation(
//            summary = "Delete tax",
//            description = "delete tax by id",
//            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                    description = "require tax id",
//                    required = true,
//                    content = @Content(schema = @Schema(implementation = String.class))
//            ),
//            responses = {
//                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "successfully deleted tax"),
//                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",description = "tax not found")
//            }
//    )
//    @DeleteMapping("/{disId}")
//    public ResponseEntity<ApiResponse<?>> deletetax(@PathVariable("disId") String disId) {
//        taxService.deletetax(disId);
//        return ResponseEntity.ok(
//                ApiResponse.builder()
//                        .status(200)
//                        .message("successfully deleted tax")
//                        .data(null)
//                        .build()
//        );
//    }
}
