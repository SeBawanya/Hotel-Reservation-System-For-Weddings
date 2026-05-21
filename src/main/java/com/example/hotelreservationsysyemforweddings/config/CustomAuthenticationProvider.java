package com.example.hotelreservationsysyemforweddings.config;

import com.example.hotelreservationsysyemforweddings.service.AdminUserDetailsService;
import com.example.hotelreservationsysyemforweddings.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private AdminUserDetailsService adminUserDetailsService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails;

        try {
            // First, try to authenticate as an Admin
            userDetails = adminUserDetailsService.loadUserByUsername(username);
            if (passwordEncoder.matches(password, userDetails.getPassword())) {
                return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
            } else {
                throw new BadCredentialsException("Invalid credentials");
            }
        } catch (UsernameNotFoundException adminNotFoundException) {
            // If not found as an Admin, try to authenticate as a Customer
            try {
                userDetails = customUserDetailsService.loadUserByUsername(username);
                if (passwordEncoder.matches(password, userDetails.getPassword())) {
                    return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
                } else {
                    throw new BadCredentialsException("Invalid credentials");
                }
            } catch (UsernameNotFoundException customerNotFoundException) {
                // If not found as a customer either, then the user does not exist
                throw new BadCredentialsException("Invalid credentials");
            }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

