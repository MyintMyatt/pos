package com.pos.features.user.model.response;

import com.pos.constant.Permission;
import com.pos.constant.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String userEmail;
    private String userName;
    private Role role;
    private Set<Permission> permissions;
    private LocalDate createdDate;
    private String profileImgUrl;
    private boolean isAccountIsActive;
    private boolean isAccountNotLocked;
}
