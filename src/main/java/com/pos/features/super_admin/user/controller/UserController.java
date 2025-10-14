package com.pos.features.super_admin.user.controller;

import com.pos.common.ApiResponse;
import com.pos.features.super_admin.user.model.request.UserRequest;
import com.pos.features.super_admin.user.service.UserService;
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
@RequestMapping("/super-admin/user")
@Tag(name = "User API", description = "Endpoints for managing users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(
            summary = "User Registration",
            description = "Add a new user to system",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Register new user",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserRequest.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201",description = "successfully registered user!!"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500",description = "internal server error")
            }
    )
    @PostMapping("/register")
//    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequest userRequest) {
        System.out.println(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse
                        .builder()
                        .status(201)
                        .message("successfully registered user!!")
                        .data(userService.saveUser(userRequest))
                        .build()
        );
    }

    @Operation(
            summary = "Get all users",
            description = "Retrieve all users from system",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "all registered users list"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500",description = "internal server error")
            }
    )
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllUsers() {
        return ResponseEntity.status(200).body(
                ApiResponse.builder()
                        .status(200)
                        .message("all registered users list")
                        .data(userService.getAllUser())
                        .build()
        );
    }
    @Operation(
            summary = "get user by user id",
            description = "retrieve specific user by id ",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "retrieve user",
                    required = true,
                    content = @Content(schema = @Schema(implementation = String.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201",description = "user who equals with user id"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",description = "user not found")
            }
    )
    @GetMapping("/{user-id}")
    public ResponseEntity<ApiResponse<Object>> getUserByEmail(@PathVariable("user-id") String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.builder()
                        .status(200)
                        .message("user who equals " + userId)
                        .data(userService.getUserByEmail(userId))
                        .build()
        );
    }
    @Operation(
            summary = "Update user",
            description = "update user information",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "update user",
                    required = true,
                    content = {
                            @Content(schema = @Schema(implementation = String.class)),
                            @Content(schema = @Schema(implementation = UserRequest.class)),
                    }
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201",description = "successfully registered user!!"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500",description = "internal server error")
            }
    )
    @PutMapping("/{user-id}")
    public ResponseEntity<ApiResponse<?>> updateUser(@PathVariable("user-id") String userId, @RequestBody UserRequest obj) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.builder()
                        .status(200)
                        .message("successfully updated user")
                        .data(userService.updateUser(userId, obj))
                        .build()
        );
    }

    @Operation(
            summary = "Disable or Enable user",
            description = "disable user or enable user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "require user id",
                    required = true,
                    content = @Content(schema = @Schema(implementation = String.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "successfully updated user"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",description = "user not found")
            }
    )
    @PutMapping("/is-disabled/{user-id}")
    public ResponseEntity<ApiResponse<?>> disableOrEnableUser(@PathVariable("user-id") String userId) {
        return ResponseEntity.status(200)
                .body(ApiResponse.builder()
                        .status(200)
                        .message("successfully updated user")
                        .data(userService.disableOrEnableUser(userId))
                        .build()
                );
    }

    @Operation(
            summary = "Delete user",
            description = "delete user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "require user id",
                    required = true,
                    content = @Content(schema = @Schema(implementation = String.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "successfully deleted user"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",description = "user not found")
            }
    )
    @DeleteMapping("/{user-id}")
    public ResponseEntity<ApiResponse<?>> deleteUser(@PathVariable("user-id") String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(200)
                        .message("successfully deleted user")
                        .data(null)
                        .build()
        );
    }
}
