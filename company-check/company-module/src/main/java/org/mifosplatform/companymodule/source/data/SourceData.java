package org.mifosplatform.companymodule.source.data;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SourceData implements Serializable {

    private Long id;
    private String name;
    private String status;
    private String createdOn;
    private String createdBy;
    private String approvedOn;
    private String approvedBy;
    private String rejectedOn;
    private String rejectedBy;
    private String lastModifiedOn;
    private String lastModifiedBy;


    public static SourceData instance(final Long id, final String name, final String status, final  String createdOn, final String createdBy,
                                      final String approvedOn, final String approvedBy, final String rejectedOn, final String rejectedBy,
                                      final String lastModifiedOn, final String lastModifiedBy) {

        return new SourceData(id, name, status,createdOn,createdBy,approvedOn,approvedBy,rejectedOn,rejectedBy, lastModifiedOn,lastModifiedBy);
    }

    private SourceData(final Long id, final String name, final String status, final  String createdOn, final String createdBy,
                       final String approvedOn, final String approvedBy, final String rejectedOn, final String rejectedBy,
                       final String lastModifiedOn, final String lastModifiedBy) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
        this.approvedOn = approvedOn;
        this.approvedBy = approvedBy;
        this.rejectedOn = rejectedOn;
        this.rejectedBy = rejectedBy;
        this.lastModifiedOn = lastModifiedOn;
        this.lastModifiedBy = lastModifiedBy;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getApprovedOn() {
        return approvedOn;
    }

    public void setApprovedOn(String approvedOn) {
        this.approvedOn = approvedOn;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getRejectedOn() {
        return rejectedOn;
    }

    public void setRejectedOn(String rejectedOn) {
        this.rejectedOn = rejectedOn;
    }

    public String getRejectedBy() {
        return rejectedBy;
    }

    public void setRejectedBy(String rejectedBy) {
        this.rejectedBy = rejectedBy;
    }

    public String getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(String lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
}
