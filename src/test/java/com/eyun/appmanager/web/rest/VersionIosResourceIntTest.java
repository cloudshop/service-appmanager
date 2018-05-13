package com.eyun.appmanager.web.rest;

import com.eyun.appmanager.AppmanagerApp;

import com.eyun.appmanager.config.SecurityBeanOverrideConfiguration;

import com.eyun.appmanager.domain.VersionIos;
import com.eyun.appmanager.repository.VersionIosRepository;
import com.eyun.appmanager.service.VersionIosService;
import com.eyun.appmanager.service.dto.VersionIosDTO;
import com.eyun.appmanager.service.mapper.VersionIosMapper;
import com.eyun.appmanager.web.rest.errors.ExceptionTranslator;
import com.eyun.appmanager.service.dto.VersionIosCriteria;
import com.eyun.appmanager.service.VersionIosQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.eyun.appmanager.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the VersionIosResource REST controller.
 *
 * @see VersionIosResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AppmanagerApp.class, SecurityBeanOverrideConfiguration.class})
public class VersionIosResourceIntTest {

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final Integer DEFAULT_VERSION_CODE = 1;
    private static final Integer UPDATED_VERSION_CODE = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_FORCE_UPDATE = false;
    private static final Boolean UPDATED_FORCE_UPDATE = true;

    private static final Instant DEFAULT_CREATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private VersionIosRepository versionIosRepository;

    @Autowired
    private VersionIosMapper versionIosMapper;

    @Autowired
    private VersionIosService versionIosService;

    @Autowired
    private VersionIosQueryService versionIosQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVersionIosMockMvc;

    private VersionIos versionIos;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VersionIosResource versionIosResource = new VersionIosResource(versionIosService, versionIosQueryService);
        this.restVersionIosMockMvc = MockMvcBuilders.standaloneSetup(versionIosResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VersionIos createEntity(EntityManager em) {
        VersionIos versionIos = new VersionIos()
            .version(DEFAULT_VERSION)
            .versionCode(DEFAULT_VERSION_CODE)
            .description(DEFAULT_DESCRIPTION)
            .forceUpdate(DEFAULT_FORCE_UPDATE)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME);
        return versionIos;
    }

    @Before
    public void initTest() {
        versionIos = createEntity(em);
    }

    @Test
    @Transactional
    public void createVersionIos() throws Exception {
        int databaseSizeBeforeCreate = versionIosRepository.findAll().size();

        // Create the VersionIos
        VersionIosDTO versionIosDTO = versionIosMapper.toDto(versionIos);
        restVersionIosMockMvc.perform(post("/api/version-ios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(versionIosDTO)))
            .andExpect(status().isCreated());

        // Validate the VersionIos in the database
        List<VersionIos> versionIosList = versionIosRepository.findAll();
        assertThat(versionIosList).hasSize(databaseSizeBeforeCreate + 1);
        VersionIos testVersionIos = versionIosList.get(versionIosList.size() - 1);
        assertThat(testVersionIos.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testVersionIos.getVersionCode()).isEqualTo(DEFAULT_VERSION_CODE);
        assertThat(testVersionIos.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testVersionIos.isForceUpdate()).isEqualTo(DEFAULT_FORCE_UPDATE);
        assertThat(testVersionIos.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testVersionIos.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void createVersionIosWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = versionIosRepository.findAll().size();

        // Create the VersionIos with an existing ID
        versionIos.setId(1L);
        VersionIosDTO versionIosDTO = versionIosMapper.toDto(versionIos);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVersionIosMockMvc.perform(post("/api/version-ios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(versionIosDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VersionIos in the database
        List<VersionIos> versionIosList = versionIosRepository.findAll();
        assertThat(versionIosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllVersionIos() throws Exception {
        // Initialize the database
        versionIosRepository.saveAndFlush(versionIos);

        // Get all the versionIosList
        restVersionIosMockMvc.perform(get("/api/version-ios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(versionIos.getId().intValue())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())))
            .andExpect(jsonPath("$.[*].versionCode").value(hasItem(DEFAULT_VERSION_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].forceUpdate").value(hasItem(DEFAULT_FORCE_UPDATE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(DEFAULT_UPDATED_TIME.toString())));
    }

    @Test
    @Transactional
    public void getVersionIos() throws Exception {
        // Initialize the database
        versionIosRepository.saveAndFlush(versionIos);

        // Get the versionIos
        restVersionIosMockMvc.perform(get("/api/version-ios/{id}", versionIos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(versionIos.getId().intValue()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION.toString()))
            .andExpect(jsonPath("$.versionCode").value(DEFAULT_VERSION_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.forceUpdate").value(DEFAULT_FORCE_UPDATE.booleanValue()))
            .andExpect(jsonPath("$.createdTime").value(DEFAULT_CREATED_TIME.toString()))
            .andExpect(jsonPath("$.updatedTime").value(DEFAULT_UPDATED_TIME.toString()));
    }

    @Test
    @Transactional
    public void getAllVersionIosByVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        versionIosRepository.saveAndFlush(versionIos);

        // Get all the versionIosList where version equals to DEFAULT_VERSION
        defaultVersionIosShouldBeFound("version.equals=" + DEFAULT_VERSION);

        // Get all the versionIosList where version equals to UPDATED_VERSION
        defaultVersionIosShouldNotBeFound("version.equals=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    public void getAllVersionIosByVersionIsInShouldWork() throws Exception {
        // Initialize the database
        versionIosRepository.saveAndFlush(versionIos);

        // Get all the versionIosList where version in DEFAULT_VERSION or UPDATED_VERSION
        defaultVersionIosShouldBeFound("version.in=" + DEFAULT_VERSION + "," + UPDATED_VERSION);

        // Get all the versionIosList where version equals to UPDATED_VERSION
        defaultVersionIosShouldNotBeFound("version.in=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    public void getAllVersionIosByVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        versionIosRepository.saveAndFlush(versionIos);

        // Get all the versionIosList where version is not null
        defaultVersionIosShouldBeFound("version.specified=true");

        // Get all the versionIosList where version is null
        defaultVersionIosShouldNotBeFound("version.specified=false");
    }

    @Test
    @Transactional
    public void getAllVersionIosByVersionCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        versionIosRepository.saveAndFlush(versionIos);

        // Get all the versionIosList where versionCode equals to DEFAULT_VERSION_CODE
        defaultVersionIosShouldBeFound("versionCode.equals=" + DEFAULT_VERSION_CODE);

        // Get all the versionIosList where versionCode equals to UPDATED_VERSION_CODE
        defaultVersionIosShouldNotBeFound("versionCode.equals=" + UPDATED_VERSION_CODE);
    }

    @Test
    @Transactional
    public void getAllVersionIosByVersionCodeIsInShouldWork() throws Exception {
        // Initialize the database
        versionIosRepository.saveAndFlush(versionIos);

        // Get all the versionIosList where versionCode in DEFAULT_VERSION_CODE or UPDATED_VERSION_CODE
        defaultVersionIosShouldBeFound("versionCode.in=" + DEFAULT_VERSION_CODE + "," + UPDATED_VERSION_CODE);

        // Get all the versionIosList where versionCode equals to UPDATED_VERSION_CODE
        defaultVersionIosShouldNotBeFound("versionCode.in=" + UPDATED_VERSION_CODE);
    }

    @Test
    @Transactional
    public void getAllVersionIosByVersionCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        versionIosRepository.saveAndFlush(versionIos);

        // Get all the versionIosList where versionCode is not null
        defaultVersionIosShouldBeFound("versionCode.specified=true");

        // Get all the versionIosList where versionCode is null
        defaultVersionIosShouldNotBeFound("versionCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllVersionIosByVersionCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        versionIosRepository.saveAndFlush(versionIos);

        // Get all the versionIosList where versionCode greater than or equals to DEFAULT_VERSION_CODE
        defaultVersionIosShouldBeFound("versionCode.greaterOrEqualThan=" + DEFAULT_VERSION_CODE);

        // Get all the versionIosList where versionCode greater than or equals to UPDATED_VERSION_CODE
        defaultVersionIosShouldNotBeFound("versionCode.greaterOrEqualThan=" + UPDATED_VERSION_CODE);
    }

    @Test
    @Transactional
    public void getAllVersionIosByVersionCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        versionIosRepository.saveAndFlush(versionIos);

        // Get all the versionIosList where versionCode less than or equals to DEFAULT_VERSION_CODE
        defaultVersionIosShouldNotBeFound("versionCode.lessThan=" + DEFAULT_VERSION_CODE);

        // Get all the versionIosList where versionCode less than or equals to UPDATED_VERSION_CODE
        defaultVersionIosShouldBeFound("versionCode.lessThan=" + UPDATED_VERSION_CODE);
    }


    @Test
    @Transactional
    public void getAllVersionIosByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        versionIosRepository.saveAndFlush(versionIos);

        // Get all the versionIosList where description equals to DEFAULT_DESCRIPTION
        defaultVersionIosShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the versionIosList where description equals to UPDATED_DESCRIPTION
        defaultVersionIosShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllVersionIosByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        versionIosRepository.saveAndFlush(versionIos);

        // Get all the versionIosList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultVersionIosShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the versionIosList where description equals to UPDATED_DESCRIPTION
        defaultVersionIosShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllVersionIosByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        versionIosRepository.saveAndFlush(versionIos);

        // Get all the versionIosList where description is not null
        defaultVersionIosShouldBeFound("description.specified=true");

        // Get all the versionIosList where description is null
        defaultVersionIosShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllVersionIosByForceUpdateIsEqualToSomething() throws Exception {
        // Initialize the database
        versionIosRepository.saveAndFlush(versionIos);

        // Get all the versionIosList where forceUpdate equals to DEFAULT_FORCE_UPDATE
        defaultVersionIosShouldBeFound("forceUpdate.equals=" + DEFAULT_FORCE_UPDATE);

        // Get all the versionIosList where forceUpdate equals to UPDATED_FORCE_UPDATE
        defaultVersionIosShouldNotBeFound("forceUpdate.equals=" + UPDATED_FORCE_UPDATE);
    }

    @Test
    @Transactional
    public void getAllVersionIosByForceUpdateIsInShouldWork() throws Exception {
        // Initialize the database
        versionIosRepository.saveAndFlush(versionIos);

        // Get all the versionIosList where forceUpdate in DEFAULT_FORCE_UPDATE or UPDATED_FORCE_UPDATE
        defaultVersionIosShouldBeFound("forceUpdate.in=" + DEFAULT_FORCE_UPDATE + "," + UPDATED_FORCE_UPDATE);

        // Get all the versionIosList where forceUpdate equals to UPDATED_FORCE_UPDATE
        defaultVersionIosShouldNotBeFound("forceUpdate.in=" + UPDATED_FORCE_UPDATE);
    }

    @Test
    @Transactional
    public void getAllVersionIosByForceUpdateIsNullOrNotNull() throws Exception {
        // Initialize the database
        versionIosRepository.saveAndFlush(versionIos);

        // Get all the versionIosList where forceUpdate is not null
        defaultVersionIosShouldBeFound("forceUpdate.specified=true");

        // Get all the versionIosList where forceUpdate is null
        defaultVersionIosShouldNotBeFound("forceUpdate.specified=false");
    }

    @Test
    @Transactional
    public void getAllVersionIosByCreatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        versionIosRepository.saveAndFlush(versionIos);

        // Get all the versionIosList where createdTime equals to DEFAULT_CREATED_TIME
        defaultVersionIosShouldBeFound("createdTime.equals=" + DEFAULT_CREATED_TIME);

        // Get all the versionIosList where createdTime equals to UPDATED_CREATED_TIME
        defaultVersionIosShouldNotBeFound("createdTime.equals=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllVersionIosByCreatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        versionIosRepository.saveAndFlush(versionIos);

        // Get all the versionIosList where createdTime in DEFAULT_CREATED_TIME or UPDATED_CREATED_TIME
        defaultVersionIosShouldBeFound("createdTime.in=" + DEFAULT_CREATED_TIME + "," + UPDATED_CREATED_TIME);

        // Get all the versionIosList where createdTime equals to UPDATED_CREATED_TIME
        defaultVersionIosShouldNotBeFound("createdTime.in=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllVersionIosByCreatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        versionIosRepository.saveAndFlush(versionIos);

        // Get all the versionIosList where createdTime is not null
        defaultVersionIosShouldBeFound("createdTime.specified=true");

        // Get all the versionIosList where createdTime is null
        defaultVersionIosShouldNotBeFound("createdTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllVersionIosByUpdatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        versionIosRepository.saveAndFlush(versionIos);

        // Get all the versionIosList where updatedTime equals to DEFAULT_UPDATED_TIME
        defaultVersionIosShouldBeFound("updatedTime.equals=" + DEFAULT_UPDATED_TIME);

        // Get all the versionIosList where updatedTime equals to UPDATED_UPDATED_TIME
        defaultVersionIosShouldNotBeFound("updatedTime.equals=" + UPDATED_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllVersionIosByUpdatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        versionIosRepository.saveAndFlush(versionIos);

        // Get all the versionIosList where updatedTime in DEFAULT_UPDATED_TIME or UPDATED_UPDATED_TIME
        defaultVersionIosShouldBeFound("updatedTime.in=" + DEFAULT_UPDATED_TIME + "," + UPDATED_UPDATED_TIME);

        // Get all the versionIosList where updatedTime equals to UPDATED_UPDATED_TIME
        defaultVersionIosShouldNotBeFound("updatedTime.in=" + UPDATED_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllVersionIosByUpdatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        versionIosRepository.saveAndFlush(versionIos);

        // Get all the versionIosList where updatedTime is not null
        defaultVersionIosShouldBeFound("updatedTime.specified=true");

        // Get all the versionIosList where updatedTime is null
        defaultVersionIosShouldNotBeFound("updatedTime.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultVersionIosShouldBeFound(String filter) throws Exception {
        restVersionIosMockMvc.perform(get("/api/version-ios?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(versionIos.getId().intValue())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())))
            .andExpect(jsonPath("$.[*].versionCode").value(hasItem(DEFAULT_VERSION_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].forceUpdate").value(hasItem(DEFAULT_FORCE_UPDATE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(DEFAULT_UPDATED_TIME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultVersionIosShouldNotBeFound(String filter) throws Exception {
        restVersionIosMockMvc.perform(get("/api/version-ios?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingVersionIos() throws Exception {
        // Get the versionIos
        restVersionIosMockMvc.perform(get("/api/version-ios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVersionIos() throws Exception {
        // Initialize the database
        versionIosRepository.saveAndFlush(versionIos);
        int databaseSizeBeforeUpdate = versionIosRepository.findAll().size();

        // Update the versionIos
        VersionIos updatedVersionIos = versionIosRepository.findOne(versionIos.getId());
        // Disconnect from session so that the updates on updatedVersionIos are not directly saved in db
        em.detach(updatedVersionIos);
        updatedVersionIos
            .version(UPDATED_VERSION)
            .versionCode(UPDATED_VERSION_CODE)
            .description(UPDATED_DESCRIPTION)
            .forceUpdate(UPDATED_FORCE_UPDATE)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME);
        VersionIosDTO versionIosDTO = versionIosMapper.toDto(updatedVersionIos);

        restVersionIosMockMvc.perform(put("/api/version-ios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(versionIosDTO)))
            .andExpect(status().isOk());

        // Validate the VersionIos in the database
        List<VersionIos> versionIosList = versionIosRepository.findAll();
        assertThat(versionIosList).hasSize(databaseSizeBeforeUpdate);
        VersionIos testVersionIos = versionIosList.get(versionIosList.size() - 1);
        assertThat(testVersionIos.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testVersionIos.getVersionCode()).isEqualTo(UPDATED_VERSION_CODE);
        assertThat(testVersionIos.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVersionIos.isForceUpdate()).isEqualTo(UPDATED_FORCE_UPDATE);
        assertThat(testVersionIos.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testVersionIos.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingVersionIos() throws Exception {
        int databaseSizeBeforeUpdate = versionIosRepository.findAll().size();

        // Create the VersionIos
        VersionIosDTO versionIosDTO = versionIosMapper.toDto(versionIos);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVersionIosMockMvc.perform(put("/api/version-ios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(versionIosDTO)))
            .andExpect(status().isCreated());

        // Validate the VersionIos in the database
        List<VersionIos> versionIosList = versionIosRepository.findAll();
        assertThat(versionIosList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVersionIos() throws Exception {
        // Initialize the database
        versionIosRepository.saveAndFlush(versionIos);
        int databaseSizeBeforeDelete = versionIosRepository.findAll().size();

        // Get the versionIos
        restVersionIosMockMvc.perform(delete("/api/version-ios/{id}", versionIos.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<VersionIos> versionIosList = versionIosRepository.findAll();
        assertThat(versionIosList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VersionIos.class);
        VersionIos versionIos1 = new VersionIos();
        versionIos1.setId(1L);
        VersionIos versionIos2 = new VersionIos();
        versionIos2.setId(versionIos1.getId());
        assertThat(versionIos1).isEqualTo(versionIos2);
        versionIos2.setId(2L);
        assertThat(versionIos1).isNotEqualTo(versionIos2);
        versionIos1.setId(null);
        assertThat(versionIos1).isNotEqualTo(versionIos2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VersionIosDTO.class);
        VersionIosDTO versionIosDTO1 = new VersionIosDTO();
        versionIosDTO1.setId(1L);
        VersionIosDTO versionIosDTO2 = new VersionIosDTO();
        assertThat(versionIosDTO1).isNotEqualTo(versionIosDTO2);
        versionIosDTO2.setId(versionIosDTO1.getId());
        assertThat(versionIosDTO1).isEqualTo(versionIosDTO2);
        versionIosDTO2.setId(2L);
        assertThat(versionIosDTO1).isNotEqualTo(versionIosDTO2);
        versionIosDTO1.setId(null);
        assertThat(versionIosDTO1).isNotEqualTo(versionIosDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(versionIosMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(versionIosMapper.fromId(null)).isNull();
    }
}
