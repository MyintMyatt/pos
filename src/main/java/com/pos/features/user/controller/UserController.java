package com.pos.features.user.controller;

import com.pos.common.ApiResponse;
import com.pos.features.user.model.request.UserRequest;
import com.pos.features.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


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

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllUsers(){
        return ResponseEntity.status(200).body(
                ApiResponse.builder()
                        .status(200)
                        .message("all registered users list")
                        .data(userService.getAllUser())
                        .build()
        );
    }

    @GetMapping("/{email}")
    public ResponseEntity<ApiResponse<Object>> getUserByEmail(@PathVariable("email") String email){
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.builder()
                        .status(200)
                        .message("user who equals " + email)
                        .data(userService.getUserByEmail(email))
                        .build()
        );
    }

    @PutMapping("/{email}")
    public ResponseEntity<ApiResponse<?>> updateUser(@PathVariable("email") String email, @RequestBody UserRequest obj){
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.builder()
                        .status(200)
                        .message("successfully updated user")
                        .data(userService.updateUser(email,obj))
                        .build()
        );
    }

//    @DeleteMapping
}
