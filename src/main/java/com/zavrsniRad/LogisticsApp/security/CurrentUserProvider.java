package com.zavrsniRad.LogisticsApp.security;

import com.zavrsniRad.LogisticsApp.domain.enums.UserRole;
import com.zavrsniRad.LogisticsApp.domain.models.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CurrentUserProvider {

    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof LogisticsUserDetails userDetails))
        {
            throw new AccessDeniedException("No authenticated user found");
        }

        return userDetails.getUser();
    }

    public boolean isStaff(){
        UserRole role = getCurrentUser().getRole();
        return role == UserRole.ADMIN || role == UserRole.DISPATCHER;
    }

    public boolean isOwnerOrStaff(UUID ownerId){
        User currentUser = getCurrentUser();

        if (currentUser.getId().equals(ownerId)) {
            return true;
        }

        UserRole role = currentUser.getRole();
        return role == UserRole.ADMIN || role == UserRole.DISPATCHER;
    }

}
