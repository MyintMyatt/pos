package com.pos.features.super_admin.menu_n_category.controller;

import com.cloudinary.Api;
import com.pos.common.model.response.ApiResponse;
import com.pos.common.service.CloudinaryService;
import com.pos.constant.InventoryMovementType;
import com.pos.features.super_admin.menu_n_category.model.request.CategoryUpdateRequest;
import com.pos.features.super_admin.menu_n_category.model.request.MenuCreateRequest;
import com.pos.features.super_admin.menu_n_category.model.request.MenuUpdateRequest;
import com.pos.features.super_admin.menu_n_category.model.response.MenuResponse;
import com.pos.features.super_admin.menu_n_category.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/menu")
@Tag(name = "Admin Menu Api", description = "Endpoints for managing menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Value("${cloudinary.menu.image.folder-name}")
    private String menuImageFolderName;

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
    public ResponseEntity<ApiResponse<?>> createMenu(@Valid @RequestBody MenuCreateRequest obj){
        if (obj.getMovementType() != InventoryMovementType.RESTOCK) throw new RuntimeException("Movement Type should be RESTOCK for menu creation");
        MenuResponse menuResponse = menuService.createMenu(obj);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.builder()
                                .status(201)
                                .message("successfully created menu!!")
                                .data(menuResponse).build()
                );
    }

    @Operation(
            summary = "menu image upload",
            description = "Upload image to cloud",
            parameters = {
                    @Parameter(
                            name = "file",
                            description = "upload file",
                            required = true,
                            schema = @Schema(implementation = MultipartFile.class)
                    )
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "successfully uploaded !!"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")

            }
    )
    @PostMapping("/image-upload/{menuId}")
    public ResponseEntity<?> uploadMenuImage(@PathVariable(name = "menuId") String menuId, @RequestParam("file") MultipartFile file){

        java.util.Map map = cloudinaryService.uploadFile(file, menuImageFolderName);
        System.err.println("image data => " + map.get("url"));
        menuService.updateMenuImage(menuId, map.get("url").toString());
        return ResponseEntity.status(HttpStatus.OK)
                .body(map);
    }


    @Operation(
            summary = "Get all menu",
            description = "Retrieve all menu from system",
            parameters = {
                    @Parameter(name = "page", description = "Page number (0-based)", required = false),
                    @Parameter(name = "size", description = "Number of items per page", required = false),
                    @Parameter(name = "keyword", description = "Search keyword for menu name", required = false),
                    @Parameter(name = "categoryId", description = "Filter by category ID", required = false)
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "all registered menu list"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500",description = "internal server error")
            }
    )
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllMenu(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String categoryId
    ) {
        return ResponseEntity.status(200).body(
                ApiResponse.builder()
                        .status(200)
                        .message("all registered menu list")
                        .data(menuService.getAllMenu(page, size, keyword, categoryId))
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
    public ResponseEntity<ApiResponse<?>> updateCategory(@PathVariable("menuId") String menuId,@Valid @RequestBody MenuUpdateRequest obj) {
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
