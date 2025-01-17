package com.investorriskreturnprofiling.aml.service.mapper;

import static com.investorriskreturnprofiling.aml.domain.PersonalDetailsAsserts.*;
import static com.investorriskreturnprofiling.aml.domain.PersonalDetailsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersonalDetailsMapperTest {

    private PersonalDetailsMapper personalDetailsMapper;

    @BeforeEach
    void setUp() {
        personalDetailsMapper = new PersonalDetailsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPersonalDetailsSample1();
        var actual = personalDetailsMapper.toEntity(personalDetailsMapper.toDto(expected));
        assertPersonalDetailsAllPropertiesEquals(expected, actual);
    }
}
