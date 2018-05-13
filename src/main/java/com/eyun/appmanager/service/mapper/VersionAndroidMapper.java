package com.eyun.appmanager.service.mapper;

import com.eyun.appmanager.domain.*;
import com.eyun.appmanager.service.dto.VersionAndroidDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity VersionAndroid and its DTO VersionAndroidDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VersionAndroidMapper extends EntityMapper<VersionAndroidDTO, VersionAndroid> {



    default VersionAndroid fromId(Long id) {
        if (id == null) {
            return null;
        }
        VersionAndroid versionAndroid = new VersionAndroid();
        versionAndroid.setId(id);
        return versionAndroid;
    }
}
