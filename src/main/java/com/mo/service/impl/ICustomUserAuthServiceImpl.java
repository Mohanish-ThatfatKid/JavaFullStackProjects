package com.mo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mo.domain.UserRole;
import com.mo.model.HostUser;
import com.mo.model.User;
import com.mo.service.ICustomUserAuthService;
import com.mo.service.IHostUserService;
import com.mo.service.IUserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ICustomUserAuthServiceImpl implements ICustomUserAuthService{

	private final IUserService userService;
    private final IHostUserService hostService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Search User table
        User user = userService.getUserByEmail(username);
        if (user != null) {
            return buildUserDetails(user.getEmail(), user.getPassword(), UserRole.USER);
        }

        // Search HostUser table
        HostUser hostUser = hostService.getHostUserByEmail(username);
        if (hostUser != null) {
            return buildUserDetails(hostUser.getEmail(), hostUser.getPassword(), UserRole.HOST);
        }

        throw new UsernameNotFoundException("User not found with provided email address");
    }

    private UserDetails buildUserDetails(String email, String password, UserRole role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        return new org.springframework.security.core.userdetails.User(email, password, authorities);
    }
	
}
