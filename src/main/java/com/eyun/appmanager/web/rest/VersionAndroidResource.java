package com.eyun.appmanager.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyun.appmanager.service.VersionAndroidService;
import com.eyun.appmanager.web.rest.errors.BadRequestAlertException;
import com.eyun.appmanager.web.rest.util.HeaderUtil;
import com.eyun.appmanager.web.rest.util.PaginationUtil;
import com.eyun.appmanager.service.dto.VersionAndroidDTO;
import com.eyun.appmanager.service.dto.VersionAndroidCriteria;
import com.eyun.appmanager.domain.VersionAndroid;
import com.eyun.appmanager.domain.VersionAndroidVO;
import com.eyun.appmanager.service.VersionAndroidQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;

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
 * REST controller for managing VersionAndroid.
 */
@RestController
@RequestMapping("/api")
public class VersionAndroidResource {

    private final Logger log = LoggerFactory.getLogger(VersionAndroidResource.class);

    private static final String ENTITY_NAME = "versionAndroid";

    private final VersionAndroidService versionAndroidService;

    private final VersionAndroidQueryService versionAndroidQueryService;

    public VersionAndroidResource(VersionAndroidService versionAndroidService, VersionAndroidQueryService versionAndroidQueryService) {
        this.versionAndroidService = versionAndroidService;
        this.versionAndroidQueryService = versionAndroidQueryService;
    }

    /**
     * POST  /version-androids : Create a new versionAndroid.
     *
     * @param versionAndroidDTO the versionAndroidDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new versionAndroidDTO, or with status 400 (Bad Request) if the versionAndroid has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/version-androids")
    @Timed
    public ResponseEntity<VersionAndroidDTO> createVersionAndroid(@RequestBody VersionAndroidDTO versionAndroidDTO) throws URISyntaxException {
        log.debug("REST request to save VersionAndroid : {}", versionAndroidDTO);
        if (versionAndroidDTO.getId() != null) {
            throw new BadRequestAlertException("A new versionAndroid cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VersionAndroidDTO result = versionAndroidService.save(versionAndroidDTO);
        return ResponseEntity.created(new URI("/api/version-androids/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /version-androids : Updates an existing versionAndroid.
     *
     * @param versionAndroidDTO the versionAndroidDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated versionAndroidDTO,
     * or with status 400 (Bad Request) if the versionAndroidDTO is not valid,
     * or with status 500 (Internal Server Error) if the versionAndroidDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/version-androids")
    @Timed
    public ResponseEntity<VersionAndroidDTO> updateVersionAndroid(@RequestBody VersionAndroidDTO versionAndroidDTO) throws URISyntaxException {
        log.debug("REST request to update VersionAndroid : {}", versionAndroidDTO);
        if (versionAndroidDTO.getId() == null) {
            return createVersionAndroid(versionAndroidDTO);
        }
        VersionAndroidDTO result = versionAndroidService.save(versionAndroidDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, versionAndroidDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /version-androids : get all the versionAndroids.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of versionAndroids in body
     */
    @GetMapping("/version-androids")
    @Timed
    public ResponseEntity<List<VersionAndroidDTO>> getAllVersionAndroids(VersionAndroidCriteria criteria, Pageable pageable) {
        log.debug("REST request to get VersionAndroids by criteria: {}", criteria);
        Page<VersionAndroidDTO> page = versionAndroidQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/version-androids");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /version-androids/:id : get the "id" versionAndroid.
     *
     * @param id the id of the versionAndroidDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the versionAndroidDTO, or with status 404 (Not Found)
     */
    @GetMapping("/version-androids/{id}")
    @Timed
    public ResponseEntity<VersionAndroidDTO> getVersionAndroid(@PathVariable Long id) {
        log.debug("REST request to get VersionAndroid : {}", id);
        VersionAndroidDTO versionAndroidDTO = versionAndroidService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(versionAndroidDTO));
    }

    /**
     * DELETE  /version-androids/:id : delete the "id" versionAndroid.
     *
     * @param id the id of the versionAndroidDTO to delete
     * @return the ResponseEntity with status 200 (OK)
    @DeleteMapping("/version-androids/{id}")
    @Timed
    public ResponseEntity<Void> deleteVersionAndroid(@PathVariable Long id) {
        log.debug("REST request to delete VersionAndroid : {}", id);
        versionAndroidService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
     */
    
    /**
     * android版本校验接口
     * @author 逍遥子
     * @email 756898059@qq.com
     * @date 2018年4月23日
     * @version 1.0
     * @param version
     * @return
     */
    @ApiOperation("android版本校验接口")
    @GetMapping("/version/android")
    @Timed
    public ResponseEntity<VersionAndroidVO> findVersionAndroid(@RequestParam("version") String version) {
    	VersionAndroidVO versionAndroidVO = versionAndroidService.findVersionAndroid(version);
    	return ResponseUtil.wrapOrNotFound(Optional.ofNullable(versionAndroidVO));
    }
    
}
