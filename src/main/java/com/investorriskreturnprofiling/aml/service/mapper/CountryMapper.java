package com.investorriskreturnprofiling.aml.service.mapper;

import com.investorriskreturnprofiling.aml.domain.Country;
import com.investorriskreturnprofiling.aml.service.dto.CountryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Country} and its DTO {@link CountryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CountryMapper extends EntityMapper<CountryDTO, Country> {}
