package com.investorriskreturnprofiling.aml.web.rest;

import static com.investorriskreturnprofiling.aml.domain.PersonalDetailsAsserts.*;
import static com.investorriskreturnprofiling.aml.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.investorriskreturnprofiling.aml.IntegrationTest;
import com.investorriskreturnprofiling.aml.domain.PersonalDetails;
import com.investorriskreturnprofiling.aml.repository.PersonalDetailsRepository;
import com.investorriskreturnprofiling.aml.service.dto.PersonalDetailsDTO;
import com.investorriskreturnprofiling.aml.service.mapper.PersonalDetailsMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PersonalDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonalDetailsResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_OF_BIRTH = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_OF_BIRTH = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/personal-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PersonalDetailsRepository personalDetailsRepository;

    @Autowired
    private PersonalDetailsMapper personalDetailsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonalDetailsMockMvc;

    private PersonalDetails personalDetails;

    private PersonalDetails insertedPersonalDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonalDetails createEntity() {
        return new PersonalDetails()
            .fullName(DEFAULT_FULL_NAME)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .comments(DEFAULT_COMMENTS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonalDetails createUpdatedEntity() {
        return new PersonalDetails()
            .fullName(UPDATED_FULL_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .comments(UPDATED_COMMENTS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    public void initTest() {
        personalDetails = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPersonalDetails != null) {
            personalDetailsRepository.delete(insertedPersonalDetails);
            insertedPersonalDetails = null;
        }
    }

    @Test
    @Transactional
    void createPersonalDetails() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PersonalDetails
        PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.toDto(personalDetails);
        var returnedPersonalDetailsDTO = om.readValue(
            restPersonalDetailsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personalDetailsDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PersonalDetailsDTO.class
        );

        // Validate the PersonalDetails in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPersonalDetails = personalDetailsMapper.toEntity(returnedPersonalDetailsDTO);
        assertPersonalDetailsUpdatableFieldsEquals(returnedPersonalDetails, getPersistedPersonalDetails(returnedPersonalDetails));

        insertedPersonalDetails = returnedPersonalDetails;
    }

    @Test
    @Transactional
    void createPersonalDetailsWithExistingId() throws Exception {
        // Create the PersonalDetails with an existing ID
        personalDetails.setId(1L);
        PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.toDto(personalDetails);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonalDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personalDetailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PersonalDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFullNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        personalDetails.setFullName(null);

        // Create the PersonalDetails, which fails.
        PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.toDto(personalDetails);

        restPersonalDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personalDetailsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateOfBirthIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        personalDetails.setDateOfBirth(null);

        // Create the PersonalDetails, which fails.
        PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.toDto(personalDetails);

        restPersonalDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personalDetailsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        personalDetails.setCreatedAt(null);

        // Create the PersonalDetails, which fails.
        PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.toDto(personalDetails);

        restPersonalDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personalDetailsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPersonalDetails() throws Exception {
        // Initialize the database
        insertedPersonalDetails = personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList
        restPersonalDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personalDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getPersonalDetails() throws Exception {
        // Initialize the database
        insertedPersonalDetails = personalDetailsRepository.saveAndFlush(personalDetails);

        // Get the personalDetails
        restPersonalDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, personalDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personalDetails.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPersonalDetails() throws Exception {
        // Get the personalDetails
        restPersonalDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPersonalDetails() throws Exception {
        // Initialize the database
        insertedPersonalDetails = personalDetailsRepository.saveAndFlush(personalDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the personalDetails
        PersonalDetails updatedPersonalDetails = personalDetailsRepository.findById(personalDetails.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPersonalDetails are not directly saved in db
        em.detach(updatedPersonalDetails);
        updatedPersonalDetails
            .fullName(UPDATED_FULL_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .comments(UPDATED_COMMENTS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.toDto(updatedPersonalDetails);

        restPersonalDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personalDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(personalDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the PersonalDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPersonalDetailsToMatchAllProperties(updatedPersonalDetails);
    }

    @Test
    @Transactional
    void putNonExistingPersonalDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personalDetails.setId(longCount.incrementAndGet());

        // Create the PersonalDetails
        PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.toDto(personalDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personalDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(personalDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonalDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personalDetails.setId(longCount.incrementAndGet());

        // Create the PersonalDetails
        PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.toDto(personalDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(personalDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonalDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personalDetails.setId(longCount.incrementAndGet());

        // Create the PersonalDetails
        PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.toDto(personalDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personalDetailsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonalDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonalDetailsWithPatch() throws Exception {
        // Initialize the database
        insertedPersonalDetails = personalDetailsRepository.saveAndFlush(personalDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the personalDetails using partial update
        PersonalDetails partialUpdatedPersonalDetails = new PersonalDetails();
        partialUpdatedPersonalDetails.setId(personalDetails.getId());

        partialUpdatedPersonalDetails.comments(UPDATED_COMMENTS);

        restPersonalDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPersonalDetails))
            )
            .andExpect(status().isOk());

        // Validate the PersonalDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersonalDetailsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPersonalDetails, personalDetails),
            getPersistedPersonalDetails(personalDetails)
        );
    }

    @Test
    @Transactional
    void fullUpdatePersonalDetailsWithPatch() throws Exception {
        // Initialize the database
        insertedPersonalDetails = personalDetailsRepository.saveAndFlush(personalDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the personalDetails using partial update
        PersonalDetails partialUpdatedPersonalDetails = new PersonalDetails();
        partialUpdatedPersonalDetails.setId(personalDetails.getId());

        partialUpdatedPersonalDetails
            .fullName(UPDATED_FULL_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .comments(UPDATED_COMMENTS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restPersonalDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPersonalDetails))
            )
            .andExpect(status().isOk());

        // Validate the PersonalDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersonalDetailsUpdatableFieldsEquals(
            partialUpdatedPersonalDetails,
            getPersistedPersonalDetails(partialUpdatedPersonalDetails)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPersonalDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personalDetails.setId(longCount.incrementAndGet());

        // Create the PersonalDetails
        PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.toDto(personalDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personalDetailsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(personalDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonalDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personalDetails.setId(longCount.incrementAndGet());

        // Create the PersonalDetails
        PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.toDto(personalDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(personalDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonalDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personalDetails.setId(longCount.incrementAndGet());

        // Create the PersonalDetails
        PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.toDto(personalDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalDetailsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(personalDetailsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonalDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonalDetails() throws Exception {
        // Initialize the database
        insertedPersonalDetails = personalDetailsRepository.saveAndFlush(personalDetails);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the personalDetails
        restPersonalDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, personalDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return personalDetailsRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected PersonalDetails getPersistedPersonalDetails(PersonalDetails personalDetails) {
        return personalDetailsRepository.findById(personalDetails.getId()).orElseThrow();
    }

    protected void assertPersistedPersonalDetailsToMatchAllProperties(PersonalDetails expectedPersonalDetails) {
        assertPersonalDetailsAllPropertiesEquals(expectedPersonalDetails, getPersistedPersonalDetails(expectedPersonalDetails));
    }

    protected void assertPersistedPersonalDetailsToMatchUpdatableProperties(PersonalDetails expectedPersonalDetails) {
        assertPersonalDetailsAllUpdatablePropertiesEquals(expectedPersonalDetails, getPersistedPersonalDetails(expectedPersonalDetails));
    }
}
