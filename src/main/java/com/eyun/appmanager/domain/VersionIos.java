package com.eyun.appmanager.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A VersionIos.
 */
@Entity
@Table(name = "version_ios")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VersionIos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "version")
    private String version;

    @Column(name = "version_code")
    private Integer versionCode;

    @Column(name = "description")
    private String description;

    @Column(name = "force_update")
    private Boolean forceUpdate;

    @Column(name = "created_time")
    private Instant createdTime;

    @Column(name = "updated_time")
    private Instant updatedTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public VersionIos version(String version) {
        this.version = version;
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public VersionIos versionCode(Integer versionCode) {
        this.versionCode = versionCode;
        return this;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getDescription() {
        return description;
    }

    public VersionIos description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isForceUpdate() {
        return forceUpdate;
    }

    public VersionIos forceUpdate(Boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
        return this;
    }

    public void setForceUpdate(Boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public VersionIos createdTime(Instant createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public Instant getUpdatedTime() {
        return updatedTime;
    }

    public VersionIos updatedTime(Instant updatedTime) {
        this.updatedTime = updatedTime;
        return this;
    }

    public void setUpdatedTime(Instant updatedTime) {
        this.updatedTime = updatedTime;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VersionIos versionIos = (VersionIos) o;
        if (versionIos.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), versionIos.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VersionIos{" +
            "id=" + getId() +
            ", version='" + getVersion() + "'" +
            ", versionCode=" + getVersionCode() +
            ", description='" + getDescription() + "'" +
            ", forceUpdate='" + isForceUpdate() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            "}";
    }
}
