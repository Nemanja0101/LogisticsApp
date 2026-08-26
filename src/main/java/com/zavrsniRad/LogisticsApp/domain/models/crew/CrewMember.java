package com.zavrsniRad.LogisticsApp.domain.models.crew;


import com.zavrsniRad.LogisticsApp.domain.models.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "crew_members")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CrewMember {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

//    @Embedded
//    private License license;
//
//    @OneToMany(mappedBy = "crewMember")
//    private List<CrewAssignment> assignments;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }


}
