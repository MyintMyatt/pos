package com.pos.features.super_admin.user.model.response;

import com.pos.constant.Permission;
import com.pos.constant.Role;
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
    private String userId;
    private String userEmail;
    private String userName;
    private Role role;
    private Set<Permission> permissions;
    private String createdDate;
    private String profileImgUrl;
    private boolean isAccountIsActive;
    private boolean isAccountNotLocked;
}
