package com.zavrsniRad.LogisticsApp.service;

import com.zavrsniRad.LogisticsApp.domain.dto.crew.CreateUpdateCrewMember;
import com.zavrsniRad.LogisticsApp.domain.dto.crew.CrewMemberResponse;
import com.zavrsniRad.LogisticsApp.domain.enums.UserRole;
import com.zavrsniRad.LogisticsApp.domain.models.User;
import com.zavrsniRad.LogisticsApp.domain.models.crew.CrewMember;
import com.zavrsniRad.LogisticsApp.exceptions.BusinessRuleViolationException;
import com.zavrsniRad.LogisticsApp.exceptions.ResourceNotFoundException;
import com.zavrsniRad.LogisticsApp.mappers.CrewMemberMapper;
import com.zavrsniRad.LogisticsApp.mappers.LicenseMapper;
import com.zavrsniRad.LogisticsApp.repositories.CrewAssignmentRepository;
import com.zavrsniRad.LogisticsApp.repositories.CrewMemberRepository;
import com.zavrsniRad.LogisticsApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CrewMemberService {

    private final CrewMemberRepository crewMemberRepository;
    private final CrewMemberMapper crewMemberMapper;
    private final CrewAssignmentRepository crewAssignmentRepository;
    private final LicenseMapper licenseMapper;
    private final UserRepository userRepository;

    public Page<CrewMemberResponse> getAllCrewMembers(Pageable pageable) {

        return crewMemberRepository.findAll(pageable).map(crewMemberMapper::toResponse);

    }

    public CrewMemberResponse getCrewMember(UUID id) {

        CrewMember crewMember = crewMemberRepository.findWithUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crew member with ID:" + id + " doesn't exist"));

        return crewMemberMapper.toResponse(crewMember);

    }


    public CrewMemberResponse createCrewMember(CreateUpdateCrewMember createCrewMember) {

        User user = userRepository.findById(createCrewMember.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with the ID: " + createCrewMember.getUserId() + "doesn't exist"));

        if (!user.getRole().equals(UserRole.CREW_MEMBER)){
            throw new BusinessRuleViolationException("Invalid user assignment!", new ArrayList<>());
        }

        if (crewMemberRepository.existsByUserId(createCrewMember.getUserId())) {
            throw new BusinessRuleViolationException("This user already has a crew member profile", new ArrayList<>());
        }

        CrewMember crewMember = CrewMember.builder()
                .user(user)
                .license(licenseMapper.toEntity(createCrewMember.getLicense()))
                .build();

        CrewMember saved = crewMemberRepository.save(crewMember);
        return crewMemberMapper.toResponse(saved);
    }


    @Transactional
    public CrewMemberResponse updateCrewMember(UUID id, CreateUpdateCrewMember updateCrewMember) {

        CrewMember crewMember = crewMemberRepository.findWithUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crew member with ID:" + id + " doesn't exist"));

        User user = userRepository.findById(updateCrewMember.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with the ID: " + updateCrewMember.getUserId() + "doesn't exist"));

        if (!user.getRole().equals(UserRole.CREW_MEMBER)){
            throw new BusinessRuleViolationException("Invalid user assignment!", new ArrayList<>());
        }

        crewMember.setLicense(licenseMapper.toEntity(updateCrewMember.getLicense()));
        crewMember.setUser(user);

        CrewMember saved = crewMemberRepository.save(crewMember);

        return crewMemberMapper.toResponse(saved);

    }

    @Transactional
    public void deleteCrewMember(UUID id) {

        CrewMember crewMember = crewMemberRepository.findWithUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crew member with ID:" + id + " doesn't exist"));

        if (crewAssignmentRepository.existsByCrewMemberId(crewMember.getId())){
            throw new BusinessRuleViolationException("You're trying to delete a crew member already assigned to a shipment segment", new ArrayList<>());
        }

        crewMemberRepository.delete(crewMember);

    }
}
