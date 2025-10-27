package com.pos.features.super_admin.menu_n_category.controller;

import com.pos.common.model.response.ApiResponse;
import com.pos.features.super_admin.menu_n_category.model.request.CategoryUpdateRequest;
import com.pos.features.super_admin.menu_n_category.model.request.MenuCreateRequest;
import com.pos.features.super_admin.menu_n_category.model.request.MenuUpdateRequest;
import com.pos.features.super_admin.menu_n_category.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/menu")
@Tag(name = "Admin Menu Api", description = "Endpoints for managing menua")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Operation(
            summary = "Menu Creation",
            description = "Create new menu",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "menu obj request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = MenuCreateRequest.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "successfully created menu!!"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500",description = "internal server error")
            }
    )
    @PostMapping
    public ResponseEntity<?> createMenu(@RequestBody MenuCreateRequest obj){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.builder()
                                .status(201)
                                .message("successfully created menu!!")
                                .data(menuService.createMenu(obj))
                );
    }

    @Operation(
            summary = "Get all menu",
            description = "Retrieve all menu from system",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "all registered menu list"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500",description = "internal server error")
            }
    )
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllMenu() {
        return ResponseEntity.status(200).body(
                ApiResponse.builder()
                        .status(200)
                        .message("all registered menu list")
                        .data(menuService.getAllMenu())
                        .build()
        );
    }


    @Operation(
            summary = "get menu by  id",
            description = "retrieve specific menu by id ",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "retrieve menu",
                    required = true,
                    content = @Content(schema = @Schema(implementation = String.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "menu that equals by id"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",description = "menu not found")
            }
    )
    @GetMapping("/{menuId}")
    public ResponseEntity<ApiResponse<Object>> getCategoryById(@PathVariable("menuId") String menuId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.builder()
                        .status(200)
                        .message("category that equals " + menuId)
                        .data(menuService.getMenuResponseById(menuId))
                        .build()
        );
    }


    @Operation(
            summary = "Update menu",
            description = "update menu information",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "update menu",
                    required = true,
                    content = {
                            @Content(schema = @Schema(implementation = String.class)),
                            @Content(schema = @Schema(implementation = MenuUpdateRequest.class)),
                    }
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "successfully updated menu!!"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500",description = "internal server error")
            }
    )

    @PutMapping("/{menuId}")
    public ResponseEntity<ApiResponse<?>> updateCategory(@PathVariable("menuId") String menuId, @RequestBody MenuUpdateRequest obj) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.builder()
                        .status(200)
                        .message("successfully updated menu")
                        .data(menuService.updateMenu(menuId, obj))
                        .build()
        );
    }


    @Operation(
            summary = "Delete menu",
            description = "delete menu by id",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "require menu id",
                    required = true,
                    content = @Content(schema = @Schema(implementation = String.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "successfully deleted menu"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",description = "menu not found")
            }
    )
    @DeleteMapping("/{menuId}")
    public ResponseEntity<ApiResponse<?>> deleteMenu(@PathVariable("menuId") String menuId) {
        menuService.deleteMenu(menuId);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(200)
                        .message("successfully deleted menu")
                        .data(null)
                        .build()
        );
    }
}
