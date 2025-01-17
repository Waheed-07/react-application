package com.investorriskreturnprofiling.aml.service.mapper;

import static com.investorriskreturnprofiling.aml.domain.CountryAsserts.*;
import static com.investorriskreturnprofiling.aml.domain.CountryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CountryMapperTest {

    private CountryMapper countryMapper;

    @BeforeEach
    void setUp() {
        countryMapper = new CountryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCountrySample1();
        var actual = countryMapper.toEntity(countryMapper.toDto(expected));
        assertCountryAllPropertiesEquals(expected, actual);
    }
}
