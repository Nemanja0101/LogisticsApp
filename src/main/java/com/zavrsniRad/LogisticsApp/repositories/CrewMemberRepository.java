package com.zavrsniRad.LogisticsApp.repositories;

import com.zavrsniRad.LogisticsApp.domain.models.crew.CrewMember;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CrewMemberRepository extends JpaRepository<CrewMember, UUID> {

    @Override
    @EntityGraph(attributePaths = "user")
    List<CrewMember> findAll();

    @EntityGraph(attributePaths = "user")
    Optional<CrewMember> findWithUserById(UUID id);

    Optional<CrewMember> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);

}
