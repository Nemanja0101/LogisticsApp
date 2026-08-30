package com.zavrsniRad.LogisticsApp.repositories;

import com.zavrsniRad.LogisticsApp.domain.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

    @Query("""
            SELECT c
            FROM Company c
            LEFT JOIN FETCH c.memberships m
            LEFT JOIN FETCH m.user
            WHERE c.id = :id
            """)
    Optional<Company> findByIdAndFetchMembers(@Param("id")UUID id);

}
