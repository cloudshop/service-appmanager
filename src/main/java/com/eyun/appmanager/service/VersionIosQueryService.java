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

import com.eyun.appmanager.domain.VersionIos;
import com.eyun.appmanager.domain.*; // for static metamodels
import com.eyun.appmanager.repository.VersionIosRepository;
import com.eyun.appmanager.service.dto.VersionIosCriteria;

import com.eyun.appmanager.service.dto.VersionIosDTO;
import com.eyun.appmanager.service.mapper.VersionIosMapper;

/**
 * Service for executing complex queries for VersionIos entities in the database.
 * The main input is a {@link VersionIosCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VersionIosDTO} or a {@link Page} of {@link VersionIosDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VersionIosQueryService extends QueryService<VersionIos> {

    private final Logger log = LoggerFactory.getLogger(VersionIosQueryService.class);


    private final VersionIosRepository versionIosRepository;

    private final VersionIosMapper versionIosMapper;

    public VersionIosQueryService(VersionIosRepository versionIosRepository, VersionIosMapper versionIosMapper) {
        this.versionIosRepository = versionIosRepository;
        this.versionIosMapper = versionIosMapper;
    }

    /**
     * Return a {@link List} of {@link VersionIosDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VersionIosDTO> findByCriteria(VersionIosCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<VersionIos> specification = createSpecification(criteria);
        return versionIosMapper.toDto(versionIosRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VersionIosDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VersionIosDTO> findByCriteria(VersionIosCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<VersionIos> specification = createSpecification(criteria);
        final Page<VersionIos> result = versionIosRepository.findAll(specification, page);
        return result.map(versionIosMapper::toDto);
    }

    /**
     * Function to convert VersionIosCriteria to a {@link Specifications}
     */
    private Specifications<VersionIos> createSpecification(VersionIosCriteria criteria) {
        Specifications<VersionIos> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), VersionIos_.id));
            }
            if (criteria.getVersion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVersion(), VersionIos_.version));
            }
            if (criteria.getVersionCode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVersionCode(), VersionIos_.versionCode));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), VersionIos_.description));
            }
            if (criteria.getForceUpdate() != null) {
                specification = specification.and(buildSpecification(criteria.getForceUpdate(), VersionIos_.forceUpdate));
            }
            if (criteria.getCreatedTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedTime(), VersionIos_.createdTime));
            }
            if (criteria.getUpdatedTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedTime(), VersionIos_.updatedTime));
            }
        }
        return specification;
    }

}
