package com.investorriskreturnprofiling.aml.web.rest;

import com.investorriskreturnprofiling.aml.repository.PersonalDetailsRepository;
import com.investorriskreturnprofiling.aml.service.PersonalDetailsService;
import com.investorriskreturnprofiling.aml.service.dto.PersonalDetailsDTO;
import com.investorriskreturnprofiling.aml.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.investorriskreturnprofiling.aml.domain.PersonalDetails}.
 */
@RestController
@RequestMapping("/api/personal-details")
public class PersonalDetailsResource {

    private static final Logger LOG = LoggerFactory.getLogger(PersonalDetailsResource.class);

    private static final String ENTITY_NAME = "personalDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonalDetailsService personalDetailsService;

    private final PersonalDetailsRepository personalDetailsRepository;

    public PersonalDetailsResource(PersonalDetailsService personalDetailsService, PersonalDetailsRepository personalDetailsRepository) {
        this.personalDetailsService = personalDetailsService;
        this.personalDetailsRepository = personalDetailsRepository;
    }

    /**
     * {@code POST  /personal-details} : Create a new personalDetails.
     *
     * @param personalDetailsDTO the personalDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personalDetailsDTO, or with status {@code 400 (Bad Request)} if the personalDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PersonalDetailsDTO> createPersonalDetails(@Valid @RequestBody PersonalDetailsDTO personalDetailsDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PersonalDetails : {}", personalDetailsDTO);
        if (personalDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new personalDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        personalDetailsDTO = personalDetailsService.save(personalDetailsDTO);
        return ResponseEntity.created(new URI("/api/personal-details/" + personalDetailsDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, personalDetailsDTO.getId().toString()))
            .body(personalDetailsDTO);
    }

    /**
     * {@code PUT  /personal-details/:id} : Updates an existing personalDetails.
     *
     * @param id the id of the personalDetailsDTO to save.
     * @param personalDetailsDTO the personalDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personalDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the personalDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personalDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PersonalDetailsDTO> updatePersonalDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PersonalDetailsDTO personalDetailsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PersonalDetails : {}, {}", id, personalDetailsDTO);
        if (personalDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personalDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personalDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        personalDetailsDTO = personalDetailsService.update(personalDetailsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personalDetailsDTO.getId().toString()))
            .body(personalDetailsDTO);
    }

    /**
     * {@code PATCH  /personal-details/:id} : Partial updates given fields of an existing personalDetails, field will ignore if it is null
     *
     * @param id the id of the personalDetailsDTO to save.
     * @param personalDetailsDTO the personalDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personalDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the personalDetailsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the personalDetailsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the personalDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PersonalDetailsDTO> partialUpdatePersonalDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PersonalDetailsDTO personalDetailsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PersonalDetails partially : {}, {}", id, personalDetailsDTO);
        if (personalDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personalDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personalDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PersonalDetailsDTO> result = personalDetailsService.partialUpdate(personalDetailsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personalDetailsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /personal-details} : get all the personalDetails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personalDetails in body.
     */
    @GetMapping("")
    public List<PersonalDetailsDTO> getAllPersonalDetails() {
        LOG.debug("REST request to get all PersonalDetails");
        return personalDetailsService.findAll();
    }

    /**
     * {@code GET  /personal-details/:id} : get the "id" personalDetails.
     *
     * @param id the id of the personalDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personalDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PersonalDetailsDTO> getPersonalDetails(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PersonalDetails : {}", id);
        Optional<PersonalDetailsDTO> personalDetailsDTO = personalDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personalDetailsDTO);
    }

    /**
     * {@code DELETE  /personal-details/:id} : delete the "id" personalDetails.
     *
     * @param id the id of the personalDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonalDetails(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PersonalDetails : {}", id);
        personalDetailsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
