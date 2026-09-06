package com.zavrsniRad.LogisticsApp.service;

import com.zavrsniRad.LogisticsApp.domain.dto.company.CompanyResponse;
import com.zavrsniRad.LogisticsApp.domain.dto.company.CreateUpdateCompany;
import com.zavrsniRad.LogisticsApp.domain.dto.company.PatchCompanyRequest;
import com.zavrsniRad.LogisticsApp.domain.dto.company.SingleCompanyResponse;
import com.zavrsniRad.LogisticsApp.domain.models.Company;
import com.zavrsniRad.LogisticsApp.domain.models.User;
import com.zavrsniRad.LogisticsApp.exceptions.BusinessRuleViolationException;
import com.zavrsniRad.LogisticsApp.exceptions.ResourceNotFoundException;
import com.zavrsniRad.LogisticsApp.mappers.CompanyMapper;
import com.zavrsniRad.LogisticsApp.mappers.UserMapper;
import com.zavrsniRad.LogisticsApp.repositories.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyMapper companyMapper;
    private final CompanyRepository companyRepository;
    private final UserMapper userMapper;

    public Page<CompanyResponse> getAllCompanies(Pageable pageable) {

        return companyRepository.findAll(pageable).map(companyMapper::toResponse);

    }

    @Transactional
    public SingleCompanyResponse getCompany(UUID id) {

        Company company = companyRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Company with the ID:" + id + " doesn't exist"));

        return companyMapper.toSingleCompany(company);

    }

    public SingleCompanyResponse createCompany(CreateUpdateCompany createCompany) {

        Company created = companyMapper.toEntity(createCompany);
        Company saved = companyRepository.save(created);

        return companyMapper.toSingleCompany(saved);
    }

    @Transactional
    public void deleteCompany(UUID id) {

        Company company = companyRepository.findByIdAndFetchMembers(id)
                .orElseThrow(()-> new ResourceNotFoundException("Company with the ID:" + id + " doesn't exist"));

        if (!company.getMemberships().isEmpty()){
            throw new BusinessRuleViolationException(
                    "Company still has users associated with it",
                    company.getMemberships()
                            .stream()
                            .map(
                                    uc -> {
                                        User u = uc.getUser();
                                        return u.getFirstName() + " " + u.getLastName() + " <" + u.getEmail() + ">";
                                    }
                            )
                            .toList()
            );
        }


        companyRepository.delete(company);
    }

    @Transactional
    public SingleCompanyResponse updateCompany(CreateUpdateCompany updateRequest, UUID id) {

        Company company = companyRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Company with the ID:" + id + " doesn't exist"));

        company.setName(updateRequest.getName());
        company.setRegistrationNumber(updateRequest.getRegistrationNumber());

        Company saved = companyRepository.save(company);

        return companyMapper.toSingleCompany(saved);

    }


    @Transactional
    public SingleCompanyResponse patchCompany(PatchCompanyRequest patchRequest, UUID id) {

        Company company = companyRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Company with the ID:" + id + " doesn't exist"));


        if (patchRequest.getName()!= null){
            company.setName(patchRequest.getName());
        }

        if (patchRequest.getRegistrationNumber()!=null){
            company.setRegistrationNumber(patchRequest.getRegistrationNumber());
        }

        Company saved = companyRepository.save(company);

        return companyMapper.toSingleCompany(saved);

    }
}
