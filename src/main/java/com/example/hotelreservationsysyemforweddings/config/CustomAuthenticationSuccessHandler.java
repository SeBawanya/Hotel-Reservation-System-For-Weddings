// src/main/java/com/example/hotelreservationsysyemforweddings/config/CustomAuthenticationSuccessHandler.java

package com.example.hotelreservationsysyemforweddings.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String redirectUrl = "/login.html?error=true"; // Default fallback

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // START OF MODIFICATION
        // The special redirect for IT Officer has been removed.
        // Now, all admin roles are checked in this single block and redirected to the main dashboard.
        if (authorities.stream().anyMatch(a ->
                a.getAuthority().equals("ROLE_IT_OFFICER") ||
                        a.getAuthority().equals("ROLE_HOTEL_MANAGER") ||
                        a.getAuthority().equals("ROLE_COORDINATOR") ||
                        a.getAuthority().equals("ROLE_RECEPTIONIST"))) {
            redirectUrl = "/admin-dashboard.html";
        }
        // END OF MODIFICATION

        // Finally, check for the customer role
        else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))) {
            redirectUrl = "/index.html";
        }

        new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}