package com.eyun.appmanager.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.eyun.appmanager.domain.VersionAndroid;
import com.eyun.appmanager.domain.*; // for static metamodels
import com.eyun.appmanager.repository.VersionAndroidRepository;
import com.eyun.appmanager.service.dto.VersionAndroidCriteria;

import com.eyun.appmanager.service.dto.VersionAndroidDTO;
import com.eyun.appmanager.service.mapper.VersionAndroidMapper;

/**
 * Service for executing complex queries for VersionAndroid entities in the database.
 * The main input is a {@link VersionAndroidCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VersionAndroidDTO} or a {@link Page} of {@link VersionAndroidDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VersionAndroidQueryService extends QueryService<VersionAndroid> {

    private final Logger log = LoggerFactory.getLogger(VersionAndroidQueryService.class);


    private final VersionAndroidRepository versionAndroidRepository;

    private final VersionAndroidMapper versionAndroidMapper;

    public VersionAndroidQueryService(VersionAndroidRepository versionAndroidRepository, VersionAndroidMapper versionAndroidMapper) {
        this.versionAndroidRepository = versionAndroidRepository;
        this.versionAndroidMapper = versionAndroidMapper;
    }

    /**
     * Return a {@link List} of {@link VersionAndroidDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VersionAndroidDTO> findByCriteria(VersionAndroidCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<VersionAndroid> specification = createSpecification(criteria);
        return versionAndroidMapper.toDto(versionAndroidRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VersionAndroidDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VersionAndroidDTO> findByCriteria(VersionAndroidCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<VersionAndroid> specification = createSpecification(criteria);
        final Page<VersionAndroid> result = versionAndroidRepository.findAll(specification, page);
        return result.map(versionAndroidMapper::toDto);
    }

    /**
     * Function to convert VersionAndroidCriteria to a {@link Specifications}
     */
    private Specifications<VersionAndroid> createSpecification(VersionAndroidCriteria criteria) {
        Specifications<VersionAndroid> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), VersionAndroid_.id));
            }
            if (criteria.getVersion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVersion(), VersionAndroid_.version));
            }
            if (criteria.getVersionCode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVersionCode(), VersionAndroid_.versionCode));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), VersionAndroid_.description));
            }
            if (criteria.getApkUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getApkUrl(), VersionAndroid_.apkUrl));
            }
            if (criteria.getForceUpdate() != null) {
                specification = specification.and(buildSpecification(criteria.getForceUpdate(), VersionAndroid_.forceUpdate));
            }
            if (criteria.getCreatedTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedTime(), VersionAndroid_.createdTime));
            }
            if (criteria.getUpdatedTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedTime(), VersionAndroid_.updatedTime));
            }
        }
        return specification;
    }

}
