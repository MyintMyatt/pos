package com.pos.features.super_admin.user.service;

import com.pos.common.service.JwtService;
import com.pos.constant.Permission;
import com.pos.exception.UserAlreadyExitedException;
import com.pos.exception.NotFoundException;
import com.pos.features.super_admin.user.model.entity.User;
import com.pos.features.super_admin.user.model.request.LoginUserRequest;
import com.pos.features.super_admin.user.model.request.UserRequest;
import com.pos.features.super_admin.user.model.response.UserResponse;
import com.pos.features.super_admin.user.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Value("${default.user.profile.image}")
    private String defaultProfileImage;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Transactional
    public UserResponse saveUser(UserRequest obj) {

        if (userRepo.findById(obj.getUserEmail()).isPresent())
            throw new UserAlreadyExitedException("User with email " + obj.getUserEmail() + " already exists.");

        obj.setPassword(passwordEncoder.encode(obj.getPassword()));
        return convertUserToUserResponse(userRepo.save(convertUserRequestToUser(obj, false)));
    }

    @Transactional
    public UserResponse getUserById(String id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("user not found with email " + id));
        return convertUserToUserResponse(user);
    }

    @Transactional
    public User getUser(String id){
        User user = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("user not found with id " + id));
        return user;
    }

    @Transactional
    public Map<String, Object> login(LoginUserRequest loginObj) {
        Map<String, Object> map = new HashMap<>();
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginObj.userId(), loginObj.password()));
        if (auth.isAuthenticated()) {
            User user = userRepo.findById(loginObj.userId()).get();
            String token = jwtService.generateToken(user);

            map.put("token", token);
            map.put("user", convertUserToUserResponse(user));
            return map;
        }
        return (Map<String, Object>) map.put("error", "email and password validation error");
    }

    @Transactional
    public List<UserResponse> getAllUser() {
        return userRepo.findAll().stream()
                .filter(u -> !u.isDeleted())
                .map(this::convertUserToUserResponse)
                .collect(Collectors.toList());
    }


    private User convertUserRequestToUser(UserRequest obj, boolean isUpdate) {
        return User.builder()
                .userEmail(obj.getUserEmail())
                .userName(obj.getUserName())
                .password(obj.getPassword())
                .role(obj.getRole())
                .permissions(obj.getPermissions())
                .profileImgUrl(defaultProfileImage)
                .createdDate(LocalDate.now())
                .updateDate(isUpdate ? LocalDate.now() : null)
                .isDeleted(false)
                .deletedDate(null)
                .isEnabled(true)
                .isAccountIsActive(true)
                .isAccountNotLocked(true)
                .build();
    }

    public UserResponse convertUserToUserResponse(User obj) {
        Set<Permission> permissions = new HashSet<>(obj.getPermissions());
        return UserResponse.builder()
                .userId(obj.getUserId())
                .userEmail(obj.getUserEmail())
                .userName(obj.getUserName())
                .role(obj.getRole())
                .permissions(permissions)
                .profileImgUrl(obj.getProfileImgUrl())
                .createdDate(obj.getCreatedDate())
                .isAccountIsActive(obj.isAccountIsActive())
                .isAccountNotLocked(obj.isAccountNotLocked())
                .build();
    }

    @Transactional
    public UserResponse updateUser(String userId, UserRequest obj) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found with email " + userId));
        return convertUserToUserResponse(userRepo.save(convertUserRequestToUser(obj, true)));
    }

    @Transactional
    public void deleteUser(String userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found with email " + userId));
        user.setDeleted(true);
        user.setDeletedDate(LocalDate.now());
        userRepo.save(user);
    }

    @Transactional
    public UserResponse disableOrEnableUser(String email) {
        User user = userRepo.findById(email)
                .orElseThrow(() -> new NotFoundException("User not found with email " + email));
        user.setAccountIsActive(user.isAccountIsActive() ? false : true);
        user.setUpdateDate(LocalDate.now());
        return convertUserToUserResponse(userRepo.save(user));
    }
}
