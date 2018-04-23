package com.eyun.appmanager.repository;

import com.eyun.appmanager.domain.VersionAndroid;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the VersionAndroid entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VersionAndroidRepository extends JpaRepository<VersionAndroid, Long>, JpaSpecificationExecutor<VersionAndroid> {

	VersionAndroid findByVersion(String version);

	List<VersionAndroid> findByOrderByCreatedTimeDesc();

}
