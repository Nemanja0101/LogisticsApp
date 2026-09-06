package com.zavrsniRad.LogisticsApp.controllers;

import com.zavrsniRad.LogisticsApp.domain.dto.user.*;
import com.zavrsniRad.LogisticsApp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @PageableDefault(sort = {"createdAt", "id"}, direction = Sort.Direction.DESC) Pageable pageable
    ){

        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers(pageable));

    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateUserResponse> createUser(
            @Valid @RequestBody CreateUser createUser
    ){

        CreateUserResponse user = userService.createUser(createUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);

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
