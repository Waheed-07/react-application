package com.investorriskreturnprofiling.aml.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.investorriskreturnprofiling.aml.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonalDetailsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonalDetailsDTO.class);
        PersonalDetailsDTO personalDetailsDTO1 = new PersonalDetailsDTO();
        personalDetailsDTO1.setId(1L);
        PersonalDetailsDTO personalDetailsDTO2 = new PersonalDetailsDTO();
        assertThat(personalDetailsDTO1).isNotEqualTo(personalDetailsDTO2);
        personalDetailsDTO2.setId(personalDetailsDTO1.getId());
        assertThat(personalDetailsDTO1).isEqualTo(personalDetailsDTO2);
        personalDetailsDTO2.setId(2L);
        assertThat(personalDetailsDTO1).isNotEqualTo(personalDetailsDTO2);
        personalDetailsDTO1.setId(null);
        assertThat(personalDetailsDTO1).isNotEqualTo(personalDetailsDTO2);
    }
}
