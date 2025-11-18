package com.pos.features.super_admin.user.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.pos.common.model.response.ResponseView;
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

    @JsonView(ResponseView.Simple.class)
    private String userId;

    @JsonView(ResponseView.Simple.class)
    private String userName;

    @JsonView(ResponseView.Full.class)
    private String userEmail;

    @JsonView(ResponseView.Full.class)
    private Role role;

    @JsonView(ResponseView.Full.class)
    private Set<Permission> permissions;

    @JsonView(ResponseView.Full.class)
    private LocalDate createdDate;

    @JsonView(ResponseView.Full.class)
    private String profileImgUrl;

    @JsonView(ResponseView.Full.class)
    private boolean isAccountIsActive;

    @JsonView(ResponseView.Full.class)
    private boolean isAccountNotLocked;
}
