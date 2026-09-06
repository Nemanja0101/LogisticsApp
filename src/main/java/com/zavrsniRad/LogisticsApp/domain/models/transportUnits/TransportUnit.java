package com.zavrsniRad.LogisticsApp.domain.models.transportUnits;


import com.zavrsniRad.LogisticsApp.domain.enums.Capability;
import com.zavrsniRad.LogisticsApp.domain.enums.TransportUnitType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "transport_units")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "transport_type")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class TransportUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ElementCollection(fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    @CollectionTable(
            name = "transport_unit_capabilities",
            joinColumns = @JoinColumn(name = "transport_unit_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "capability", nullable = false)
    @Builder.Default
    private Set<Capability> capabilities = new HashSet<>();

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }

    public abstract TransportUnitType getType();

}


