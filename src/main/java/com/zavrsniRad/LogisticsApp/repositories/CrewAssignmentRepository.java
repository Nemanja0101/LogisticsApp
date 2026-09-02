package com.zavrsniRad.LogisticsApp.repositories;

import com.zavrsniRad.LogisticsApp.domain.models.crew.CrewAssignment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CrewAssignmentRepository extends JpaRepository<CrewAssignment, UUID> {

    @Override
    @EntityGraph(attributePaths = {"crewMember", "crewMember.user", "shipmentSegment"})
    List<CrewAssignment> findAll();

    @EntityGraph(attributePaths = {"crewMember", "crewMember.user", "shipmentSegment"})
    Optional<CrewAssignment> findWithDetailsById(UUID id);

    @EntityGraph(attributePaths = {"crewMember", "crewMember.user", "shipmentSegment"})
    List<CrewAssignment> findAllByShipmentSegmentId(UUID shipmentSegmentId);

    @EntityGraph(attributePaths = {"crewMember", "crewMember.user", "shipmentSegment"})
    List<CrewAssignment> findAllByShipmentSegmentIdIn(Collection<UUID> shipmentSegmentIds);

    @EntityGraph(attributePaths = {"crewMember", "crewMember.user", "shipmentSegment"})
    List<CrewAssignment> findAllByCrewMemberId(UUID crewMemberId);

    boolean existsByCrewMemberIdAndShipmentSegmentId(UUID crewMemberId, UUID shipmentSegmentId);

    boolean existsByCrewMemberId(UUID crewMemberId);

}
