package com.zavrsniRad.LogisticsApp.domain.models;


import com.zavrsniRad.LogisticsApp.domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

//    unique je dodat kroz parcijelni index-unique
    @Column(nullable = false)
    private String email;

    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user")
    private List<UserCompany> companyMemberships;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "deactivated_at")
    @Builder.Default
    private LocalDateTime deactivatedAt = null;

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email) && role == user.role && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(password, user.password) && Objects.equals(createdAt, user.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, role, firstName, lastName, password, createdAt);
    }
}
