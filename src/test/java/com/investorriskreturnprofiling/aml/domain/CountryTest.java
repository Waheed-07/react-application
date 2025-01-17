package com.investorriskreturnprofiling.aml.domain;

import static com.investorriskreturnprofiling.aml.domain.CountryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.investorriskreturnprofiling.aml.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CountryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Country.class);
        Country country1 = getCountrySample1();
        Country country2 = new Country();
        assertThat(country1).isNotEqualTo(country2);

        country2.setId(country1.getId());
        assertThat(country1).isEqualTo(country2);

        country2 = getCountrySample2();
        assertThat(country1).isNotEqualTo(country2);
    }
}
