package com.zavrsniRad.LogisticsApp.service;

import com.zavrsniRad.LogisticsApp.domain.dto.company.member.AddCompanyMember;
import com.zavrsniRad.LogisticsApp.domain.dto.company.member.CompanyMemberResponse;
import com.zavrsniRad.LogisticsApp.domain.models.Company;
import com.zavrsniRad.LogisticsApp.domain.models.User;
import com.zavrsniRad.LogisticsApp.domain.models.UserCompany;
import com.zavrsniRad.LogisticsApp.exceptions.BusinessRuleViolationException;
import com.zavrsniRad.LogisticsApp.exceptions.ResourceNotFoundException;
import com.zavrsniRad.LogisticsApp.mappers.CompanyMemberMapper;
import com.zavrsniRad.LogisticsApp.repositories.CompanyRepository;
import com.zavrsniRad.LogisticsApp.repositories.UserCompanyRepository;
import com.zavrsniRad.LogisticsApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyMemberService {

    private final UserCompanyRepository userCompanyRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final CompanyMemberMapper companyMemberMapper;

    @Transactional
    public List<CompanyMemberResponse> getCompanyMembers(UUID companyId) {

        if (!companyRepository.existsById(companyId)) {
            throw new ResourceNotFoundException("Company with the ID: " + companyId + " doesn't exist");
        }

        return userCompanyRepository.findAllByCompanyId(companyId)
                .stream()
                .map(companyMemberMapper::toResponse)
                .toList();

    }

    @Transactional
    public CompanyMemberResponse addCompanyMember(UUID companyId, AddCompanyMember addCompanyMember) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company with the ID: " + companyId + " doesn't exist"));

        User user = userRepository.findByIdAndDeactivatedAtIsNull(addCompanyMember.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with the ID: " + addCompanyMember.getUserId() + " doesn't exist"));

        if (userCompanyRepository.existsByCompanyIdAndUserId(companyId, user.getId())) {
            throw new BusinessRuleViolationException(
                    "User is already a member of this company",
                    new ArrayList<>(List.of(user.getEmail()))
            );
        }

        UserCompany membership = UserCompany.builder()
                .company(company)
                .user(user)
                .build();

        UserCompany saved = userCompanyRepository.save(membership);

        return companyMemberMapper.toResponse(saved);

    }

    @Transactional
    public void removeCompanyMember(UUID companyId, UUID userId) {

        UserCompany membership = userCompanyRepository.findByCompanyIdAndUserId(companyId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with the ID: " + userId + " is not a member of company with the ID: " + companyId));

        userCompanyRepository.delete(membership);

    }

}
