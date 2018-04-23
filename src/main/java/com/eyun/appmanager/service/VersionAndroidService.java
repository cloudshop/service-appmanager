package com.eyun.appmanager.service;

import com.eyun.appmanager.domain.VersionAndroid;
import com.eyun.appmanager.domain.VersionAndroidVO;
import com.eyun.appmanager.service.dto.VersionAndroidDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing VersionAndroid.
 */
public interface VersionAndroidService {

    /**
     * Save a versionAndroid.
     *
     * @param versionAndroidDTO the entity to save
     * @return the persisted entity
     */
    VersionAndroidDTO save(VersionAndroidDTO versionAndroidDTO);

    /**
     * Get all the versionAndroids.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<VersionAndroidDTO> findAll(Pageable pageable);

    /**
     * Get the "id" versionAndroid.
     *
     * @param id the id of the entity
     * @return the entity
     */
    VersionAndroidDTO findOne(Long id);

    /**
     * Delete the "id" versionAndroid.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

	VersionAndroidVO findVersionAndroid(String version);
	
}
