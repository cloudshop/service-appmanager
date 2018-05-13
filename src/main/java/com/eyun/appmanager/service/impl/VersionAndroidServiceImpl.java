package com.eyun.appmanager.service.impl;

import com.eyun.appmanager.service.VersionAndroidService;
import com.eyun.appmanager.domain.VersionAndroid;
import com.eyun.appmanager.domain.VersionAndroidVO;
import com.eyun.appmanager.repository.VersionAndroidRepository;
import com.eyun.appmanager.service.dto.VersionAndroidDTO;
import com.eyun.appmanager.service.mapper.VersionAndroidMapper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing VersionAndroid.
 */
@Service
@Transactional
public class VersionAndroidServiceImpl implements VersionAndroidService {

    private final Logger log = LoggerFactory.getLogger(VersionAndroidServiceImpl.class);

    private final VersionAndroidRepository versionAndroidRepository;

    private final VersionAndroidMapper versionAndroidMapper;

    public VersionAndroidServiceImpl(VersionAndroidRepository versionAndroidRepository, VersionAndroidMapper versionAndroidMapper) {
        this.versionAndroidRepository = versionAndroidRepository;
        this.versionAndroidMapper = versionAndroidMapper;
    }

    /**
     * Save a versionAndroid.
     *
     * @param versionAndroidDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public VersionAndroidDTO save(VersionAndroidDTO versionAndroidDTO) {
        log.debug("Request to save VersionAndroid : {}", versionAndroidDTO);
        VersionAndroid versionAndroid = versionAndroidMapper.toEntity(versionAndroidDTO);
        versionAndroid = versionAndroidRepository.save(versionAndroid);
        return versionAndroidMapper.toDto(versionAndroid);
    }

    /**
     * Get all the versionAndroids.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<VersionAndroidDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VersionAndroids");
        return versionAndroidRepository.findAll(pageable)
            .map(versionAndroidMapper::toDto);
    }

    /**
     * Get one versionAndroid by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public VersionAndroidDTO findOne(Long id) {
        log.debug("Request to get VersionAndroid : {}", id);
        VersionAndroid versionAndroid = versionAndroidRepository.findOne(id);
        return versionAndroidMapper.toDto(versionAndroid);
    }

    /**
     * Delete the versionAndroid by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete VersionAndroid : {}", id);
        versionAndroidRepository.delete(id);
    }

	@Override
	@Transactional(readOnly = true)
	public VersionAndroidVO findVersionAndroid(String version) {
		VersionAndroid currentVersion = versionAndroidRepository.findByVersion(version);
		List<VersionAndroid> list = versionAndroidRepository.findByOrderByCreatedTimeDesc();
		VersionAndroid newestVersion = list.get(0);
		VersionAndroidVO versionAndroidVO = new VersionAndroidVO();
		versionAndroidVO.setCurrentVersion(currentVersion);
		versionAndroidVO.setNewestVersion(newestVersion);
		return versionAndroidVO;
	}
    
}
