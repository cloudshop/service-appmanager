package com.eyun.appmanager.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyun.appmanager.service.VersionIosService;
import com.eyun.appmanager.web.rest.errors.BadRequestAlertException;
import com.eyun.appmanager.web.rest.util.HeaderUtil;
import com.eyun.appmanager.web.rest.util.PaginationUtil;
import com.eyun.appmanager.service.dto.VersionIosDTO;
import com.eyun.appmanager.service.dto.VersionIosCriteria;
import com.eyun.appmanager.service.VersionIosQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing VersionIos.
 */
@RestController
@RequestMapping("/api")
public class VersionIosResource {

    private final Logger log = LoggerFactory.getLogger(VersionIosResource.class);

    private static final String ENTITY_NAME = "versionIos";

    private final VersionIosService versionIosService;

    private final VersionIosQueryService versionIosQueryService;

    public VersionIosResource(VersionIosService versionIosService, VersionIosQueryService versionIosQueryService) {
        this.versionIosService = versionIosService;
        this.versionIosQueryService = versionIosQueryService;
    }

    /**
     * POST  /version-ios : Create a new versionIos.
     *
     * @param versionIosDTO the versionIosDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new versionIosDTO, or with status 400 (Bad Request) if the versionIos has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/version-ios")
    @Timed
    public ResponseEntity<VersionIosDTO> createVersionIos(@RequestBody VersionIosDTO versionIosDTO) throws URISyntaxException {
        log.debug("REST request to save VersionIos : {}", versionIosDTO);
        if (versionIosDTO.getId() != null) {
            throw new BadRequestAlertException("A new versionIos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VersionIosDTO result = versionIosService.save(versionIosDTO);
        return ResponseEntity.created(new URI("/api/version-ios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /version-ios : Updates an existing versionIos.
     *
     * @param versionIosDTO the versionIosDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated versionIosDTO,
     * or with status 400 (Bad Request) if the versionIosDTO is not valid,
     * or with status 500 (Internal Server Error) if the versionIosDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/version-ios")
    @Timed
    public ResponseEntity<VersionIosDTO> updateVersionIos(@RequestBody VersionIosDTO versionIosDTO) throws URISyntaxException {
        log.debug("REST request to update VersionIos : {}", versionIosDTO);
        if (versionIosDTO.getId() == null) {
            return createVersionIos(versionIosDTO);
        }
        VersionIosDTO result = versionIosService.save(versionIosDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, versionIosDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /version-ios : get all the versionIos.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of versionIos in body
     */
    @GetMapping("/version-ios")
    @Timed
    public ResponseEntity<List<VersionIosDTO>> getAllVersionIos(VersionIosCriteria criteria, Pageable pageable) {
        log.debug("REST request to get VersionIos by criteria: {}", criteria);
        Page<VersionIosDTO> page = versionIosQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/version-ios");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /version-ios/:id : get the "id" versionIos.
     *
     * @param id the id of the versionIosDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the versionIosDTO, or with status 404 (Not Found)
     */
    @GetMapping("/version-ios/{id}")
    @Timed
    public ResponseEntity<VersionIosDTO> getVersionIos(@PathVariable Long id) {
        log.debug("REST request to get VersionIos : {}", id);
        VersionIosDTO versionIosDTO = versionIosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(versionIosDTO));
    }

    /**
     * DELETE  /version-ios/:id : delete the "id" versionIos.
     *
     * @param id the id of the versionIosDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/version-ios/{id}")
    @Timed
    public ResponseEntity<Void> deleteVersionIos(@PathVariable Long id) {
        log.debug("REST request to delete VersionIos : {}", id);
        versionIosService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
