package com.eyun.appmanager.service.dto;


import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the VersionAndroid entity.
 */
public class VersionAndroidDTO implements Serializable {

    private Long id;

    private String version;

    private Integer versionCode;

    private String description;

    private String apkUrl;

    private Boolean forceUpdate;

    private Instant createdTime;

    private Instant updatedTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public Boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(Boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public Instant getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Instant updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VersionAndroidDTO versionAndroidDTO = (VersionAndroidDTO) o;
        if(versionAndroidDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), versionAndroidDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VersionAndroidDTO{" +
            "id=" + getId() +
            ", version='" + getVersion() + "'" +
            ", versionCode=" + getVersionCode() +
            ", description='" + getDescription() + "'" +
            ", apkUrl='" + getApkUrl() + "'" +
            ", forceUpdate='" + isForceUpdate() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            "}";
    }
}
