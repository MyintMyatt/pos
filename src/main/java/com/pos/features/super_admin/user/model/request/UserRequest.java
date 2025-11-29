package com.pos.features.super_admin.user.model.request;

import com.pos.constant.Permission;
import com.pos.constant.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRequest {

//    private String userId;

    @NotBlank(message = "email must not be null or empty")
    @Email(message = "invalid email format")
    private String userEmail;

    @NotBlank(message = "user name must not be null or empty")
    private String userName;

    @NotBlank(message = "email must not be null or empty")
    private String password;

    @NotNull(message = "user role must not be null")
    private Role role;

    private Set<Permission> permissions;

}
