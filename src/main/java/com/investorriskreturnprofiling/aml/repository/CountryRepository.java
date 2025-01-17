package com.investorriskreturnprofiling.aml.repository;

import com.investorriskreturnprofiling.aml.domain.Country;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Country entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {}
