package com.investorriskreturnprofiling.aml.service.mapper;

import com.investorriskreturnprofiling.aml.domain.PersonalDetails;
import com.investorriskreturnprofiling.aml.service.dto.PersonalDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PersonalDetails} and its DTO {@link PersonalDetailsDTO}.
 */
@Mapper(componentModel = "spring")
public interface PersonalDetailsMapper extends EntityMapper<PersonalDetailsDTO, PersonalDetails> {}
