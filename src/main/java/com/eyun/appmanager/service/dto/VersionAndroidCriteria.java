package com.eyun.appmanager.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import io.github.jhipster.service.filter.InstantFilter;




/**
 * Criteria class for the VersionAndroid entity. This class is used in VersionAndroidResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /version-androids?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class VersionAndroidCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter version;

    private IntegerFilter versionCode;

    private StringFilter description;

    private StringFilter apkUrl;

    private BooleanFilter forceUpdate;

    private InstantFilter createdTime;

    private InstantFilter updatedTime;

    public VersionAndroidCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getVersion() {
        return version;
    }

    public void setVersion(StringFilter version) {
        this.version = version;
    }

    public IntegerFilter getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(IntegerFilter versionCode) {
        this.versionCode = versionCode;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(StringFilter apkUrl) {
        this.apkUrl = apkUrl;
    }

    public BooleanFilter getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(BooleanFilter forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public InstantFilter getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(InstantFilter createdTime) {
        this.createdTime = createdTime;
    }

    public InstantFilter getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(InstantFilter updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public String toString() {
        return "VersionAndroidCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (version != null ? "version=" + version + ", " : "") +
                (versionCode != null ? "versionCode=" + versionCode + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (apkUrl != null ? "apkUrl=" + apkUrl + ", " : "") +
                (forceUpdate != null ? "forceUpdate=" + forceUpdate + ", " : "") +
                (createdTime != null ? "createdTime=" + createdTime + ", " : "") +
                (updatedTime != null ? "updatedTime=" + updatedTime + ", " : "") +
            "}";
    }

}
