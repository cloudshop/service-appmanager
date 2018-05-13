package com.eyun.appmanager.service;

import com.eyun.appmanager.service.dto.VersionIosDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing VersionIos.
 */
public interface VersionIosService {

    /**
     * Save a versionIos.
     *
     * @param versionIosDTO the entity to save
     * @return the persisted entity
     */
    VersionIosDTO save(VersionIosDTO versionIosDTO);

    /**
     * Get all the versionIos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<VersionIosDTO> findAll(Pageable pageable);

    /**
     * Get the "id" versionIos.
     *
     * @param id the id of the entity
     * @return the entity
     */
    VersionIosDTO findOne(Long id);

    /**
     * Delete the "id" versionIos.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
