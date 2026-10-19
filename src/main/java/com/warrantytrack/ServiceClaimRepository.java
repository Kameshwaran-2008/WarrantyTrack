package com.warrantytrack;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ServiceClaimRepository
        extends JpaRepository<ServiceClaim, Long> {

    List<ServiceClaim> findByOwner_Id(Long ownerId);

    Optional<ServiceClaim> findByIdAndOwner_Id(Long id, Long ownerId);

    long countByOwner_Id(Long ownerId);

    List<ServiceClaim> findTop5ByOwner_IdOrderByClaimDateDescIdDesc(
            Long ownerId);
}