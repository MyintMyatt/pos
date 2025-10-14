package com.pos.features.super_admin.user.model.entity;

import com.pos.constant.Permission;
import com.pos.constant.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "tbl_users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(generator = "user-id-generator")
    @GenericGenerator(
            name = "user-id-generator",
            strategy = "com.pos.features.super_admin.user.util.UserIdGenerator"
    )
    @Column(name = "user_id", length = 20, nullable = false, updatable = false)
    private String userId;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "user_permissions",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "permission")
    private Set<Permission> permissions;

    private LocalDate createdDate = LocalDate.now();

    private LocalDate updateDate;

    @Column(nullable = true)
    private boolean isDeleted = false;

    private LocalDate deletedDate;

    private boolean isEnabled = true;

    private boolean isAccountIsActive = true;

    private boolean isAccountNotLocked = true;

    private String profileImgUrl;

}

