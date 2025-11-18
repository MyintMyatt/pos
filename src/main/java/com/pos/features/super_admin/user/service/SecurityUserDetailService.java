package com.pos.features.super_admin.user.service;

import com.pos.exception.NotFoundException;
import com.pos.features.super_admin.user.model.entity.User;
import com.pos.features.super_admin.user.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SecurityUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
       User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with email " + userId));

       /*add permissions */
        Set<SimpleGrantedAuthority> authorities = user.getPermissions()
                .stream()
                .map(p -> new SimpleGrantedAuthority("ROLE_"+p.name()))
                .collect(Collectors.toSet());

        /*add role*/
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        return new org.springframework.security.core.userdetails.User(
                user.getUserId(),
                user.getPassword(),
                user.isEnabled(),
                user.isAccountIsActive(),
                true,
                user.isAccountNotLocked(),
                authorities
        );
    }
}
