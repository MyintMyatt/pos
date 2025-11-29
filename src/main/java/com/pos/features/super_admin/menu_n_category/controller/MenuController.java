package com.pos.features.super_admin.menu_n_category.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.pos.common.model.response.ApiResponse;
import com.pos.common.model.response.ResponseView;
import com.pos.common.service.CloudinaryService;
import com.pos.features.super_admin.menu_n_category.model.request.MenuRequest;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
                    content = @Content(schema = @Schema(implementation = MenuRequest.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "successfully created menu!!"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "internal server error")
            }
    )
    @PreAuthorize("hasRole('WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createMenu(@Valid @RequestBody MenuRequest obj) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.builder()
                                .status(201)
                                .message("successfully created menu!!")
                                .data(menuService.createMenu(obj)).build()
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
    @PreAuthorize("hasRole('WRITE')")
    @PostMapping("/image-upload/{menuId}")
    public ResponseEntity<?> uploadMenuImage(@PathVariable(name = "menuId") String menuId, @RequestParam("file") MultipartFile file) {
        java.util.Map map = cloudinaryService.uploadFile(file, menuImageFolderName);
        System.err.println("call");
        menuService.updateMenuImage(menuId, map.get("url").toString());
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.builder()
                        .status(200)
                        .message("successfully uploaded image")
                        .data(map.get("url"))
                        .build()
                );
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
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "all registered menu list"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "internal server error")
            }
    )
    @PreAuthorize("hasRole('READ')")
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
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "menu that equals by id"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "menu not found")
            }
    )
    @PreAuthorize("hasRole('READ')")
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
                            @Content(schema = @Schema(implementation = MenuRequest.class)),
                    }
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "successfully updated menu!!"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "internal server error")
            }
    )
    @PreAuthorize("hasRole('UPDATE')")
    @PutMapping("/{menuId}")
    public ResponseEntity<ApiResponse<?>> updateMenu(@PathVariable("menuId") String menuId, @Valid @RequestBody MenuRequest obj) {
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
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "successfully deleted menu"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "menu not found")
            }
    )
    @PreAuthorize("hasRole('DELETE')")
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
