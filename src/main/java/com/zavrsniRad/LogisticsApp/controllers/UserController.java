package com.zavrsniRad.LogisticsApp.controllers;

import com.zavrsniRad.LogisticsApp.domain.dto.user.ChangePassword;
import com.zavrsniRad.LogisticsApp.domain.dto.user.PatchUserRequest;
import com.zavrsniRad.LogisticsApp.domain.dto.user.UpdateUserRole;
import com.zavrsniRad.LogisticsApp.domain.dto.user.UserResponse;
import com.zavrsniRad.LogisticsApp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers(){

        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);

    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.user.id")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable UUID id
    ){
        UserResponse user = userService.getSingleUserAdminGated(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }


    @GetMapping("/me")
    public ResponseEntity<?> getMe(){

        UserResponse user = userService.getMe();
        return ResponseEntity.status(HttpStatus.OK).body(user);

    }


    @PatchMapping("/me")
    public ResponseEntity<UserResponse> patchMe(
            @Valid @RequestBody PatchUserRequest patchRequest
    ){

        UserResponse user = userService.patchMe(patchRequest);
        return ResponseEntity.status(HttpStatus.OK).body(user);

    }


    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ChangePassword changePasswordRequest
    ){

        userService.changePassword(changePasswordRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }


    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updateUserRole(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRole updateRoleRequest
    ){

        UserResponse user = userService.updateUserRole(id, updateRoleRequest);
        return ResponseEntity.status(HttpStatus.OK).body(user);

    }


    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe(){

        userService.deleteMe();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(
            @PathVariable UUID id
    ){

        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}
