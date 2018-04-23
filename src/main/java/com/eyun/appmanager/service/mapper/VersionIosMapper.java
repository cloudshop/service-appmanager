package com.eyun.appmanager.service.mapper;

import com.eyun.appmanager.domain.*;
import com.eyun.appmanager.service.dto.VersionIosDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity VersionIos and its DTO VersionIosDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VersionIosMapper extends EntityMapper<VersionIosDTO, VersionIos> {



    default VersionIos fromId(Long id) {
        if (id == null) {
            return null;
        }
        VersionIos versionIos = new VersionIos();
        versionIos.setId(id);
        return versionIos;
    }
}
