package com.zavrsniRad.LogisticsApp.repositories;

import com.zavrsniRad.LogisticsApp.domain.models.crew.CrewMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CrewMemberRepository extends JpaRepository<CrewMember, UUID> {

    @Override
    @EntityGraph(attributePaths = "user")
    Page<CrewMember> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "user")
    Optional<CrewMember> findWithUserById(UUID id);

    Optional<CrewMember> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);

}
