package com.pos.features.super_admin.menu_n_category.controller;

import com.pos.common.model.response.ApiResponse;
import com.pos.features.super_admin.discount.model.request.DiscountRequest;
import com.pos.features.super_admin.menu_n_category.model.request.CategoryCreateRequest;
import com.pos.features.super_admin.menu_n_category.model.request.CategoryUpdateRequest;
import com.pos.features.super_admin.menu_n_category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/category")
@Tag(name = "Admin Category Api", description = "Endpoints for managing category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(
            summary = "Category Creation",
            description = "create new category",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "category request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CategoryCreateRequest.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201",description = "successfully created new category!!"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500",description = "internal server error")
            }
    )
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryCreateRequest obj){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.builder()
                                .status(201)
                                .message("successfully created new category!!!")
                                .data(categoryService.createCategory(obj))
                                .build()
                );
    }

    @Operation(
            summary = "Get all category",
            description = "Retrieve all category from system",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "all registered category list"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500",description = "internal server error")
            }
    )
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllCategory() {
        return ResponseEntity.status(200).body(
                ApiResponse.builder()
                        .status(200)
                        .message("all registered category list")
                        .data(categoryService.getAllCat())
                        .build()
        );
    }

    @Operation(
            summary = "get category by  id",
            description = "retrieve specific category by id ",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "retrieve category",
                    required = true,
                    content = @Content(schema = @Schema(implementation = String.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "category that equals by id"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",description = "category not found")
            }
    )
    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Object>> getCategoryById(@PathVariable("categoryId") String categoryId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.builder()
                        .status(200)
                        .message("category that equals " + categoryId)
                        .data(categoryService.getCategoryById(categoryId))
                        .build()
        );
    }

    @Operation(
            summary = "Update category",
            description = "update category information",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "update category",
                    required = true,
                    content = {
                            @Content(schema = @Schema(implementation = String.class)),
                            @Content(schema = @Schema(implementation = CategoryUpdateRequest.class)),
                    }
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "successfully updated category!!"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500",description = "internal server error")
            }
    )

    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<?>> updateCategory(@PathVariable("categoryId") String categoryId, @RequestBody CategoryUpdateRequest obj) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.builder()
                        .status(200)
                        .message("successfully updated category")
                        .data(categoryService.updateCategory(categoryId, obj))
                        .build()
        );
    }


    @Operation(
            summary = "Delete category",
            description = "delete category by id",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "require category id",
                    required = true,
                    content = @Content(schema = @Schema(implementation = String.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "successfully deleted category"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",description = "category not found")
            }
    )
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<?>> deleteCategory(@PathVariable("categoryId") String categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(200)
                        .message("successfully deleted category")
                        .data(null)
                        .build()
        );
    }
}