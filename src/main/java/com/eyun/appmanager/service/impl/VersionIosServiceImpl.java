package com.eyun.appmanager.service.impl;

import com.eyun.appmanager.service.VersionIosService;
import com.eyun.appmanager.domain.VersionIos;
import com.eyun.appmanager.repository.VersionIosRepository;
import com.eyun.appmanager.service.dto.VersionIosDTO;
import com.eyun.appmanager.service.mapper.VersionIosMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing VersionIos.
 */
@Service
@Transactional
public class VersionIosServiceImpl implements VersionIosService {

    private final Logger log = LoggerFactory.getLogger(VersionIosServiceImpl.class);

    private final VersionIosRepository versionIosRepository;

    private final VersionIosMapper versionIosMapper;

    public VersionIosServiceImpl(VersionIosRepository versionIosRepository, VersionIosMapper versionIosMapper) {
        this.versionIosRepository = versionIosRepository;
        this.versionIosMapper = versionIosMapper;
    }

    /**
     * Save a versionIos.
     *
     * @param versionIosDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public VersionIosDTO save(VersionIosDTO versionIosDTO) {
        log.debug("Request to save VersionIos : {}", versionIosDTO);
        VersionIos versionIos = versionIosMapper.toEntity(versionIosDTO);
        versionIos = versionIosRepository.save(versionIos);
        return versionIosMapper.toDto(versionIos);
    }

    /**
     * Get all the versionIos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<VersionIosDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VersionIos");
        return versionIosRepository.findAll(pageable)
            .map(versionIosMapper::toDto);
    }

    /**
     * Get one versionIos by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public VersionIosDTO findOne(Long id) {
        log.debug("Request to get VersionIos : {}", id);
        VersionIos versionIos = versionIosRepository.findOne(id);
        return versionIosMapper.toDto(versionIos);
    }

    /**
     * Delete the versionIos by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete VersionIos : {}", id);
        versionIosRepository.delete(id);
    }
}
