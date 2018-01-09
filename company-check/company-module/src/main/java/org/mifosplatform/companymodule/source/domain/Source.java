package org.mifosplatform.companymodule.source.domain;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.mifosplatform.companymodule.utils.JsonHelper;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
@Table(name = "m_source")
public class Source extends AbstractPersistable<Long>{


    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "status_enum", nullable = false)
    private Integer status;

    @Column(name = "createdon_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date createdOn;

    @Column(name = "createdby_userid", nullable = true)
    private String createdBy;

    @Column(name = "approvedon_date")
    @Temporal(TemporalType.DATE)
    private Date approvedOn;

    @Column(name = "approvedby_userid", nullable = true)
    private String approvedBy;

    @Column(name = "rejectedon_date")
    @Temporal(TemporalType.DATE)
    private Date rejectedOn;

    @Column(name = "rejectedby_userid", nullable = true)
    private String rejectedBy;

    @Column(name = "lastmodifiedon_date")
    @Temporal(TemporalType.DATE)
    private Date lastModifiedOn;

    @Column(name = "lastmodifiedby_userid", nullable = true)
    private String lastModifiedBy;

    public Source() {
    }

    public Source(String name,Integer status, LocalDate createdOn, String createdBy, LocalDate approvedOn, String approvedBy,
                  LocalDate rejectedOn, String rejectedBy, LocalDate lastModifiedOn, String lastModifiedBy) {

        this.name = name;
        this.status = status;
        if (createdOn != null) {
            this.createdOn = createdOn.toDateTimeAtStartOfDay().toDate();
        }
        this.createdBy = createdBy;
        if (approvedOn != null) {
            this.approvedOn = approvedOn.toDateTimeAtStartOfDay().toDate();
        }
        this.approvedBy = approvedBy;
        if (rejectedOn != null) {
            this.rejectedOn = rejectedOn.toDateTimeAtStartOfDay().toDate();
        }
        this.rejectedBy = rejectedBy;
        if (lastModifiedOn != null) {
            this.lastModifiedOn = lastModifiedOn.toDateTimeAtStartOfDay().toDate();
        }
        this.lastModifiedBy = lastModifiedBy;
    }

    public void approve(final Date approvedDate, final String approvedUser) {
        this.approvedOn = approvedDate;
        this.approvedBy = approvedUser;
        this.status = SourceStatusType.APPROVED.getValue();
    }

    public void undoApprove() {
        this.status = SourceStatusType.PENDING_APPROVAL.getValue();
        this.approvedOn = null;
        this.approvedBy = null;
        this.rejectedOn = null;
        this.rejectedBy = null;
    }

    public void reject(final Date rejectedDate, final String rejectedUser) {
        this.rejectedOn = rejectedDate;
        this.rejectedBy = rejectedUser;
        this.status = SourceStatusType.REJECTED.getValue();
    }

    public Map<String, Object> update(final JsonHelper command, final String updatedBy) {
        final Map<String, Object> actualChanges = new LinkedHashMap<>(7);

        final String nameParamName = "name";
        if (command.isChangeInStringParameterNamed(nameParamName, this.name)) {
            final String newValue = command.stringValueOfParameterNamed(nameParamName);
            actualChanges.put(nameParamName, newValue);
            this.name = StringUtils.defaultIfEmpty(newValue, null);

            final LocalDate updatedOn = command.localDateValueOfParameterNamed("updatedOn");
            this.lastModifiedOn = updatedOn.toDate();
            this.lastModifiedBy = updatedBy;
        }

        return actualChanges;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setApprovedOn(Date approvedOn) {
        this.approvedOn = approvedOn;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public void setRejectedOn(Date rejectedOn) {
        this.rejectedOn = rejectedOn;
    }

    public void setRejectedBy(String rejectedBy) {
        this.rejectedBy = rejectedBy;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public static Source fromJson(Integer status, final String createdBy, final String approvedBy, final String rejectedBy,
                                  final String lastModifiedBy, final JsonHelper command) {

        final String name = command.stringValueOfParameterNamed("name");

        final LocalDate createdOn = command.localDateValueOfParameterNamed("createdOn");
        final LocalDate approvedOn = command.localDateValueOfParameterNamed("approvedOn");
        final LocalDate rejectedOn = command.localDateValueOfParameterNamed("rejectedOn");
        final LocalDate lastModifiedOn = command.localDateValueOfParameterNamed("lastModifiedOn");

        return new Source(name, status,createdOn,createdBy,approvedOn,approvedBy,rejectedOn,rejectedBy, lastModifiedOn,lastModifiedBy);
    }

    public String getName() {
        return name;
    }

    public Integer getStatus() {
        return status;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Date getApprovedOn() {
        return approvedOn;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public Date getRejectedOn() {
        return rejectedOn;
    }

    public String getRejectedBy() {
        return rejectedBy;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }
}
