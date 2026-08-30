package com.zavrsniRad.LogisticsApp.service;


import com.zavrsniRad.LogisticsApp.domain.dto.user.ChangePassword;
import com.zavrsniRad.LogisticsApp.domain.dto.user.PatchUserRequest;
import com.zavrsniRad.LogisticsApp.domain.dto.user.UpdateUserRole;
import com.zavrsniRad.LogisticsApp.domain.dto.user.UserResponse;
import com.zavrsniRad.LogisticsApp.domain.enums.UserRole;
import com.zavrsniRad.LogisticsApp.domain.models.User;
import com.zavrsniRad.LogisticsApp.exceptions.BusinessRuleViolationException;
import com.zavrsniRad.LogisticsApp.exceptions.ResourceNotFoundException;
import com.zavrsniRad.LogisticsApp.mappers.UserMapper;
import com.zavrsniRad.LogisticsApp.repositories.RefreshTokenRepository;
import com.zavrsniRad.LogisticsApp.repositories.UserRepository;
import com.zavrsniRad.LogisticsApp.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CurrentUserProvider currentUserProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;


    public UserResponse getSingleUserAdminGated(UUID id) {

        User user = userRepository.findByIdAndDeactivatedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID: " + id + " doesn't exist"));

        return userMapper.toResponse(user);

    }

    public UserResponse getMe() {
        return userMapper.toResponse(currentUserProvider.getCurrentUser());
    }

    public List<UserResponse> getAllUsers() {

        List<User> allUsers = userRepository.findAllByDeactivatedAtIsNull();

        return allUsers
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Transactional
    public UserResponse patchMe(PatchUserRequest patchRequest) {

        User currentUser = currentUserProvider.getCurrentUser();

        if (patchRequest.getEmail() != null){
            currentUser.setEmail(patchRequest.getEmail());
        }

        if (patchRequest.getFirstName() != null){
            currentUser.setFirstName(patchRequest.getFirstName());
        }

        if (patchRequest.getLastName() != null){
            currentUser.setLastName(patchRequest.getLastName());
        }

        User saved = userRepository.save(currentUser);

        return userMapper.toResponse(saved);

    }

    @Transactional
    public void changePassword(ChangePassword changePasswordRequest) {

        User currentUser = currentUserProvider.getCurrentUser();

        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), currentUser.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }

        currentUser.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));

        //moze i samo revoke, ali radi i ovako
        refreshTokenRepository.deleteByUserId(currentUser.getId());
        userRepository.save(currentUser);

    }

    @Transactional
    public UserResponse updateUserRole(UUID id, UpdateUserRole updateRoleRequest) {

        // ne bit trebalo da moze da se apdejture korisnik koji je deaktiviran
        // jer jedini razlog zasto nije hard delete vec soft je cuvanje record-a
        // i izbegavanje data integrity violation exception-a
        User userToUpdate = userRepository.findByIdAndDeactivatedAtIsNull(id)
                .orElseThrow(()-> new ResourceNotFoundException("User with the ID:" + id + " doesn't exist"));

        if (userToUpdate.getRole() == UserRole.ADMIN
                && updateRoleRequest.getUserRole() != UserRole.ADMIN
                && userRepository.countByRoleAndDeactivatedAtIsNull(UserRole.ADMIN) <= 1) {
            throw new BusinessRuleViolationException("Can't demote the last remaining admin");
        }

        if (updateRoleRequest.getUserRole() != null){
            userToUpdate.setRole(updateRoleRequest.getUserRole());
        }

        User saved = userRepository.save(userToUpdate);

        return userMapper.toResponse(saved);
    }

    @Transactional
    public void deleteMe() {

        User currentUser = currentUserProvider.getCurrentUser();

        if (currentUser.getRole() == UserRole.ADMIN
                && userRepository.countByRoleAndDeactivatedAtIsNull(UserRole.ADMIN) <= 1) {
            throw new BusinessRuleViolationException("Can't deactivate the last remaining admin");
        }
        currentUser.setDeactivatedAt(LocalDateTime.now());
        User saved = userRepository.save(currentUser);
        refreshTokenRepository.deleteByUserId(saved.getId());
    }


    @Transactional
    public void deleteUser(UUID id) {

        User user = userRepository.findByIdAndDeactivatedAtIsNull(id)
                .orElseThrow(()-> new ResourceNotFoundException("User with the ID: " + id + " doesn't exist"));

        if (user.getRole() == UserRole.ADMIN
                && userRepository.countByRoleAndDeactivatedAtIsNull(UserRole.ADMIN) <= 1) {
            throw new BusinessRuleViolationException("Can't deactivate the last remaining admin");
        }

        user.setDeactivatedAt(LocalDateTime.now());
        User saved = userRepository.save(user);
        refreshTokenRepository.deleteByUserId(saved.getId());
        
    }
}
