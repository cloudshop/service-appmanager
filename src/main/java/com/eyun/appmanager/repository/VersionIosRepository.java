package com.eyun.appmanager.repository;

import com.eyun.appmanager.domain.VersionIos;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the VersionIos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VersionIosRepository extends JpaRepository<VersionIos, Long>, JpaSpecificationExecutor<VersionIos> {

}
