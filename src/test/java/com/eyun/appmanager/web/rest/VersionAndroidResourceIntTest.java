package com.eyun.appmanager.web.rest;

import com.eyun.appmanager.AppmanagerApp;

import com.eyun.appmanager.config.SecurityBeanOverrideConfiguration;

import com.eyun.appmanager.domain.VersionAndroid;
import com.eyun.appmanager.repository.VersionAndroidRepository;
import com.eyun.appmanager.service.VersionAndroidService;
import com.eyun.appmanager.service.dto.VersionAndroidDTO;
import com.eyun.appmanager.service.mapper.VersionAndroidMapper;
import com.eyun.appmanager.web.rest.errors.ExceptionTranslator;
import com.eyun.appmanager.service.dto.VersionAndroidCriteria;
import com.eyun.appmanager.service.VersionAndroidQueryService;

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
 * Test class for the VersionAndroidResource REST controller.
 *
 * @see VersionAndroidResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AppmanagerApp.class, SecurityBeanOverrideConfiguration.class})
public class VersionAndroidResourceIntTest {

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final Integer DEFAULT_VERSION_CODE = 1;
    private static final Integer UPDATED_VERSION_CODE = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_APK_URL = "AAAAAAAAAA";
    private static final String UPDATED_APK_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_FORCE_UPDATE = false;
    private static final Boolean UPDATED_FORCE_UPDATE = true;

    private static final Instant DEFAULT_CREATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private VersionAndroidRepository versionAndroidRepository;

    @Autowired
    private VersionAndroidMapper versionAndroidMapper;

    @Autowired
    private VersionAndroidService versionAndroidService;

    @Autowired
    private VersionAndroidQueryService versionAndroidQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVersionAndroidMockMvc;

    private VersionAndroid versionAndroid;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VersionAndroidResource versionAndroidResource = new VersionAndroidResource(versionAndroidService, versionAndroidQueryService);
        this.restVersionAndroidMockMvc = MockMvcBuilders.standaloneSetup(versionAndroidResource)
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
    public static VersionAndroid createEntity(EntityManager em) {
        VersionAndroid versionAndroid = new VersionAndroid()
            .version(DEFAULT_VERSION)
            .versionCode(DEFAULT_VERSION_CODE)
            .description(DEFAULT_DESCRIPTION)
            .apkUrl(DEFAULT_APK_URL)
            .forceUpdate(DEFAULT_FORCE_UPDATE)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME);
        return versionAndroid;
    }

    @Before
    public void initTest() {
        versionAndroid = createEntity(em);
    }

    @Test
    @Transactional
    public void createVersionAndroid() throws Exception {
        int databaseSizeBeforeCreate = versionAndroidRepository.findAll().size();

        // Create the VersionAndroid
        VersionAndroidDTO versionAndroidDTO = versionAndroidMapper.toDto(versionAndroid);
        restVersionAndroidMockMvc.perform(post("/api/version-androids")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(versionAndroidDTO)))
            .andExpect(status().isCreated());

        // Validate the VersionAndroid in the database
        List<VersionAndroid> versionAndroidList = versionAndroidRepository.findAll();
        assertThat(versionAndroidList).hasSize(databaseSizeBeforeCreate + 1);
        VersionAndroid testVersionAndroid = versionAndroidList.get(versionAndroidList.size() - 1);
        assertThat(testVersionAndroid.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testVersionAndroid.getVersionCode()).isEqualTo(DEFAULT_VERSION_CODE);
        assertThat(testVersionAndroid.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testVersionAndroid.getApkUrl()).isEqualTo(DEFAULT_APK_URL);
        assertThat(testVersionAndroid.isForceUpdate()).isEqualTo(DEFAULT_FORCE_UPDATE);
        assertThat(testVersionAndroid.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testVersionAndroid.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void createVersionAndroidWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = versionAndroidRepository.findAll().size();

        // Create the VersionAndroid with an existing ID
        versionAndroid.setId(1L);
        VersionAndroidDTO versionAndroidDTO = versionAndroidMapper.toDto(versionAndroid);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVersionAndroidMockMvc.perform(post("/api/version-androids")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(versionAndroidDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VersionAndroid in the database
        List<VersionAndroid> versionAndroidList = versionAndroidRepository.findAll();
        assertThat(versionAndroidList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllVersionAndroids() throws Exception {
        // Initialize the database
        versionAndroidRepository.saveAndFlush(versionAndroid);

        // Get all the versionAndroidList
        restVersionAndroidMockMvc.perform(get("/api/version-androids?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(versionAndroid.getId().intValue())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())))
            .andExpect(jsonPath("$.[*].versionCode").value(hasItem(DEFAULT_VERSION_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].apkUrl").value(hasItem(DEFAULT_APK_URL.toString())))
            .andExpect(jsonPath("$.[*].forceUpdate").value(hasItem(DEFAULT_FORCE_UPDATE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(DEFAULT_UPDATED_TIME.toString())));
    }

    @Test
    @Transactional
    public void getVersionAndroid() throws Exception {
        // Initialize the database
        versionAndroidRepository.saveAndFlush(versionAndroid);

        // Get the versionAndroid
        restVersionAndroidMockMvc.perform(get("/api/version-androids/{id}", versionAndroid.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(versionAndroid.getId().intValue()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION.toString()))
            .andExpect(jsonPath("$.versionCode").value(DEFAULT_VERSION_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.apkUrl").value(DEFAULT_APK_URL.toString()))
            .andExpect(jsonPath("$.forceUpdate").value(DEFAULT_FORCE_UPDATE.booleanValue()))
            .andExpect(jsonPath("$.createdTime").value(DEFAULT_CREATED_TIME.toString()))
            .andExpect(jsonPath("$.updatedTime").value(DEFAULT_UPDATED_TIME.toString()));
    }

    @Test
    @Transactional
    public void getAllVersionAndroidsByVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        versionAndroidRepository.saveAndFlush(versionAndroid);

        // Get all the versionAndroidList where version equals to DEFAULT_VERSION
        defaultVersionAndroidShouldBeFound("version.equals=" + DEFAULT_VERSION);

        // Get all the versionAndroidList where version equals to UPDATED_VERSION
        defaultVersionAndroidShouldNotBeFound("version.equals=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    public void getAllVersionAndroidsByVersionIsInShouldWork() throws Exception {
        // Initialize the database
        versionAndroidRepository.saveAndFlush(versionAndroid);

        // Get all the versionAndroidList where version in DEFAULT_VERSION or UPDATED_VERSION
        defaultVersionAndroidShouldBeFound("version.in=" + DEFAULT_VERSION + "," + UPDATED_VERSION);

        // Get all the versionAndroidList where version equals to UPDATED_VERSION
        defaultVersionAndroidShouldNotBeFound("version.in=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    public void getAllVersionAndroidsByVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        versionAndroidRepository.saveAndFlush(versionAndroid);

        // Get all the versionAndroidList where version is not null
        defaultVersionAndroidShouldBeFound("version.specified=true");

        // Get all the versionAndroidList where version is null
        defaultVersionAndroidShouldNotBeFound("version.specified=false");
    }

    @Test
    @Transactional
    public void getAllVersionAndroidsByVersionCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        versionAndroidRepository.saveAndFlush(versionAndroid);

        // Get all the versionAndroidList where versionCode equals to DEFAULT_VERSION_CODE
        defaultVersionAndroidShouldBeFound("versionCode.equals=" + DEFAULT_VERSION_CODE);

        // Get all the versionAndroidList where versionCode equals to UPDATED_VERSION_CODE
        defaultVersionAndroidShouldNotBeFound("versionCode.equals=" + UPDATED_VERSION_CODE);
    }

    @Test
    @Transactional
    public void getAllVersionAndroidsByVersionCodeIsInShouldWork() throws Exception {
        // Initialize the database
        versionAndroidRepository.saveAndFlush(versionAndroid);

        // Get all the versionAndroidList where versionCode in DEFAULT_VERSION_CODE or UPDATED_VERSION_CODE
        defaultVersionAndroidShouldBeFound("versionCode.in=" + DEFAULT_VERSION_CODE + "," + UPDATED_VERSION_CODE);

        // Get all the versionAndroidList where versionCode equals to UPDATED_VERSION_CODE
        defaultVersionAndroidShouldNotBeFound("versionCode.in=" + UPDATED_VERSION_CODE);
    }

    @Test
    @Transactional
    public void getAllVersionAndroidsByVersionCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        versionAndroidRepository.saveAndFlush(versionAndroid);

        // Get all the versionAndroidList where versionCode is not null
        defaultVersionAndroidShouldBeFound("versionCode.specified=true");

        // Get all the versionAndroidList where versionCode is null
        defaultVersionAndroidShouldNotBeFound("versionCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllVersionAndroidsByVersionCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        versionAndroidRepository.saveAndFlush(versionAndroid);

        // Get all the versionAndroidList where versionCode greater than or equals to DEFAULT_VERSION_CODE
        defaultVersionAndroidShouldBeFound("versionCode.greaterOrEqualThan=" + DEFAULT_VERSION_CODE);

        // Get all the versionAndroidList where versionCode greater than or equals to UPDATED_VERSION_CODE
        defaultVersionAndroidShouldNotBeFound("versionCode.greaterOrEqualThan=" + UPDATED_VERSION_CODE);
    }

    @Test
    @Transactional
    public void getAllVersionAndroidsByVersionCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        versionAndroidRepository.saveAndFlush(versionAndroid);

        // Get all the versionAndroidList where versionCode less than or equals to DEFAULT_VERSION_CODE
        defaultVersionAndroidShouldNotBeFound("versionCode.lessThan=" + DEFAULT_VERSION_CODE);

        // Get all the versionAndroidList where versionCode less than or equals to UPDATED_VERSION_CODE
        defaultVersionAndroidShouldBeFound("versionCode.lessThan=" + UPDATED_VERSION_CODE);
    }


    @Test
    @Transactional
    public void getAllVersionAndroidsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        versionAndroidRepository.saveAndFlush(versionAndroid);

        // Get all the versionAndroidList where description equals to DEFAULT_DESCRIPTION
        defaultVersionAndroidShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the versionAndroidList where description equals to UPDATED_DESCRIPTION
        defaultVersionAndroidShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllVersionAndroidsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        versionAndroidRepository.saveAndFlush(versionAndroid);

        // Get all the versionAndroidList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultVersionAndroidShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the versionAndroidList where description equals to UPDATED_DESCRIPTION
        defaultVersionAndroidShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllVersionAndroidsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        versionAndroidRepository.saveAndFlush(versionAndroid);

        // Get all the versionAndroidList where description is not null
        defaultVersionAndroidShouldBeFound("description.specified=true");

        // Get all the versionAndroidList where description is null
        defaultVersionAndroidShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllVersionAndroidsByApkUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        versionAndroidRepository.saveAndFlush(versionAndroid);

        // Get all the versionAndroidList where apkUrl equals to DEFAULT_APK_URL
        defaultVersionAndroidShouldBeFound("apkUrl.equals=" + DEFAULT_APK_URL);

        // Get all the versionAndroidList where apkUrl equals to UPDATED_APK_URL
        defaultVersionAndroidShouldNotBeFound("apkUrl.equals=" + UPDATED_APK_URL);
    }

    @Test
    @Transactional
    public void getAllVersionAndroidsByApkUrlIsInShouldWork() throws Exception {
        // Initialize the database
        versionAndroidRepository.saveAndFlush(versionAndroid);

        // Get all the versionAndroidList where apkUrl in DEFAULT_APK_URL or UPDATED_APK_URL
        defaultVersionAndroidShouldBeFound("apkUrl.in=" + DEFAULT_APK_URL + "," + UPDATED_APK_URL);

        // Get all the versionAndroidList where apkUrl equals to UPDATED_APK_URL
        defaultVersionAndroidShouldNotBeFound("apkUrl.in=" + UPDATED_APK_URL);
    }

    @Test
    @Transactional
    public void getAllVersionAndroidsByApkUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        versionAndroidRepository.saveAndFlush(versionAndroid);

        // Get all the versionAndroidList where apkUrl is not null
        defaultVersionAndroidShouldBeFound("apkUrl.specified=true");

        // Get all the versionAndroidList where apkUrl is null
        defaultVersionAndroidShouldNotBeFound("apkUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllVersionAndroidsByForceUpdateIsEqualToSomething() throws Exception {
        // Initialize the database
        versionAndroidRepository.saveAndFlush(versionAndroid);

        // Get all the versionAndroidList where forceUpdate equals to DEFAULT_FORCE_UPDATE
        defaultVersionAndroidShouldBeFound("forceUpdate.equals=" + DEFAULT_FORCE_UPDATE);

        // Get all the versionAndroidList where forceUpdate equals to UPDATED_FORCE_UPDATE
        defaultVersionAndroidShouldNotBeFound("forceUpdate.equals=" + UPDATED_FORCE_UPDATE);
    }

    @Test
    @Transactional
    public void getAllVersionAndroidsByForceUpdateIsInShouldWork() throws Exception {
        // Initialize the database
        versionAndroidRepository.saveAndFlush(versionAndroid);

        // Get all the versionAndroidList where forceUpdate in DEFAULT_FORCE_UPDATE or UPDATED_FORCE_UPDATE
        defaultVersionAndroidShouldBeFound("forceUpdate.in=" + DEFAULT_FORCE_UPDATE + "," + UPDATED_FORCE_UPDATE);

        // Get all the versionAndroidList where forceUpdate equals to UPDATED_FORCE_UPDATE
        defaultVersionAndroidShouldNotBeFound("forceUpdate.in=" + UPDATED_FORCE_UPDATE);
    }

    @Test
    @Transactional
    public void getAllVersionAndroidsByForceUpdateIsNullOrNotNull() throws Exception {
        // Initialize the database
        versionAndroidRepository.saveAndFlush(versionAndroid);

        // Get all the versionAndroidList where forceUpdate is not null
        defaultVersionAndroidShouldBeFound("forceUpdate.specified=true");

        // Get all the versionAndroidList where forceUpdate is null
        defaultVersionAndroidShouldNotBeFound("forceUpdate.specified=false");
    }

    @Test
    @Transactional
    public void getAllVersionAndroidsByCreatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        versionAndroidRepository.saveAndFlush(versionAndroid);

        // Get all the versionAndroidList where createdTime equals to DEFAULT_CREATED_TIME
        defaultVersionAndroidShouldBeFound("createdTime.equals=" + DEFAULT_CREATED_TIME);

        // Get all the versionAndroidList where createdTime equals to UPDATED_CREATED_TIME
        defaultVersionAndroidShouldNotBeFound("createdTime.equals=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllVersionAndroidsByCreatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        versionAndroidRepository.saveAndFlush(versionAndroid);

        // Get all the versionAndroidList where createdTime in DEFAULT_CREATED_TIME or UPDATED_CREATED_TIME
        defaultVersionAndroidShouldBeFound("createdTime.in=" + DEFAULT_CREATED_TIME + "," + UPDATED_CREATED_TIME);

        // Get all the versionAndroidList where createdTime equals to UPDATED_CREATED_TIME
        defaultVersionAndroidShouldNotBeFound("createdTime.in=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllVersionAndroidsByCreatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        versionAndroidRepository.saveAndFlush(versionAndroid);

        // Get all the versionAndroidList where createdTime is not null
        defaultVersionAndroidShouldBeFound("createdTime.specified=true");

        // Get all the versionAndroidList where createdTime is null
        defaultVersionAndroidShouldNotBeFound("createdTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllVersionAndroidsByUpdatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        versionAndroidRepository.saveAndFlush(versionAndroid);

        // Get all the versionAndroidList where updatedTime equals to DEFAULT_UPDATED_TIME
        defaultVersionAndroidShouldBeFound("updatedTime.equals=" + DEFAULT_UPDATED_TIME);

        // Get all the versionAndroidList where updatedTime equals to UPDATED_UPDATED_TIME
        defaultVersionAndroidShouldNotBeFound("updatedTime.equals=" + UPDATED_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllVersionAndroidsByUpdatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        versionAndroidRepository.saveAndFlush(versionAndroid);

        // Get all the versionAndroidList where updatedTime in DEFAULT_UPDATED_TIME or UPDATED_UPDATED_TIME
        defaultVersionAndroidShouldBeFound("updatedTime.in=" + DEFAULT_UPDATED_TIME + "," + UPDATED_UPDATED_TIME);

        // Get all the versionAndroidList where updatedTime equals to UPDATED_UPDATED_TIME
        defaultVersionAndroidShouldNotBeFound("updatedTime.in=" + UPDATED_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllVersionAndroidsByUpdatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        versionAndroidRepository.saveAndFlush(versionAndroid);

        // Get all the versionAndroidList where updatedTime is not null
        defaultVersionAndroidShouldBeFound("updatedTime.specified=true");

        // Get all the versionAndroidList where updatedTime is null
        defaultVersionAndroidShouldNotBeFound("updatedTime.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultVersionAndroidShouldBeFound(String filter) throws Exception {
        restVersionAndroidMockMvc.perform(get("/api/version-androids?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(versionAndroid.getId().intValue())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())))
            .andExpect(jsonPath("$.[*].versionCode").value(hasItem(DEFAULT_VERSION_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].apkUrl").value(hasItem(DEFAULT_APK_URL.toString())))
            .andExpect(jsonPath("$.[*].forceUpdate").value(hasItem(DEFAULT_FORCE_UPDATE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(DEFAULT_UPDATED_TIME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultVersionAndroidShouldNotBeFound(String filter) throws Exception {
        restVersionAndroidMockMvc.perform(get("/api/version-androids?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingVersionAndroid() throws Exception {
        // Get the versionAndroid
        restVersionAndroidMockMvc.perform(get("/api/version-androids/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVersionAndroid() throws Exception {
        // Initialize the database
        versionAndroidRepository.saveAndFlush(versionAndroid);
        int databaseSizeBeforeUpdate = versionAndroidRepository.findAll().size();

        // Update the versionAndroid
        VersionAndroid updatedVersionAndroid = versionAndroidRepository.findOne(versionAndroid.getId());
        // Disconnect from session so that the updates on updatedVersionAndroid are not directly saved in db
        em.detach(updatedVersionAndroid);
        updatedVersionAndroid
            .version(UPDATED_VERSION)
            .versionCode(UPDATED_VERSION_CODE)
            .description(UPDATED_DESCRIPTION)
            .apkUrl(UPDATED_APK_URL)
            .forceUpdate(UPDATED_FORCE_UPDATE)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME);
        VersionAndroidDTO versionAndroidDTO = versionAndroidMapper.toDto(updatedVersionAndroid);

        restVersionAndroidMockMvc.perform(put("/api/version-androids")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(versionAndroidDTO)))
            .andExpect(status().isOk());

        // Validate the VersionAndroid in the database
        List<VersionAndroid> versionAndroidList = versionAndroidRepository.findAll();
        assertThat(versionAndroidList).hasSize(databaseSizeBeforeUpdate);
        VersionAndroid testVersionAndroid = versionAndroidList.get(versionAndroidList.size() - 1);
        assertThat(testVersionAndroid.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testVersionAndroid.getVersionCode()).isEqualTo(UPDATED_VERSION_CODE);
        assertThat(testVersionAndroid.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVersionAndroid.getApkUrl()).isEqualTo(UPDATED_APK_URL);
        assertThat(testVersionAndroid.isForceUpdate()).isEqualTo(UPDATED_FORCE_UPDATE);
        assertThat(testVersionAndroid.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testVersionAndroid.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingVersionAndroid() throws Exception {
        int databaseSizeBeforeUpdate = versionAndroidRepository.findAll().size();

        // Create the VersionAndroid
        VersionAndroidDTO versionAndroidDTO = versionAndroidMapper.toDto(versionAndroid);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVersionAndroidMockMvc.perform(put("/api/version-androids")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(versionAndroidDTO)))
            .andExpect(status().isCreated());

        // Validate the VersionAndroid in the database
        List<VersionAndroid> versionAndroidList = versionAndroidRepository.findAll();
        assertThat(versionAndroidList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVersionAndroid() throws Exception {
        // Initialize the database
        versionAndroidRepository.saveAndFlush(versionAndroid);
        int databaseSizeBeforeDelete = versionAndroidRepository.findAll().size();

        // Get the versionAndroid
        restVersionAndroidMockMvc.perform(delete("/api/version-androids/{id}", versionAndroid.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<VersionAndroid> versionAndroidList = versionAndroidRepository.findAll();
        assertThat(versionAndroidList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VersionAndroid.class);
        VersionAndroid versionAndroid1 = new VersionAndroid();
        versionAndroid1.setId(1L);
        VersionAndroid versionAndroid2 = new VersionAndroid();
        versionAndroid2.setId(versionAndroid1.getId());
        assertThat(versionAndroid1).isEqualTo(versionAndroid2);
        versionAndroid2.setId(2L);
        assertThat(versionAndroid1).isNotEqualTo(versionAndroid2);
        versionAndroid1.setId(null);
        assertThat(versionAndroid1).isNotEqualTo(versionAndroid2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VersionAndroidDTO.class);
        VersionAndroidDTO versionAndroidDTO1 = new VersionAndroidDTO();
        versionAndroidDTO1.setId(1L);
        VersionAndroidDTO versionAndroidDTO2 = new VersionAndroidDTO();
        assertThat(versionAndroidDTO1).isNotEqualTo(versionAndroidDTO2);
        versionAndroidDTO2.setId(versionAndroidDTO1.getId());
        assertThat(versionAndroidDTO1).isEqualTo(versionAndroidDTO2);
        versionAndroidDTO2.setId(2L);
        assertThat(versionAndroidDTO1).isNotEqualTo(versionAndroidDTO2);
        versionAndroidDTO1.setId(null);
        assertThat(versionAndroidDTO1).isNotEqualTo(versionAndroidDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(versionAndroidMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(versionAndroidMapper.fromId(null)).isNull();
    }
}
